package br.com.blueproject.transactionbff.domain;

import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import br.com.blueproject.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    private TransactionRedisRepository repository;
    private RetryTemplate retryTemplate;

    public TransactionService(TransactionRedisRepository repository, RetryTemplate retryTemplate) {
        this.repository = repository;
        this.retryTemplate = retryTemplate;
    }


    @Transactional
    @Retryable( value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    // The 'value' argument is the problem failure that trigger the retry sequence.
    public Optional<TransactionDto> save (final RequestTransactionDto requestDto) {

        requestDto.setData(LocalDateTime.now());
        return Optional.of(repository.save(requestDto));
    }


    public Optional<TransactionDto> findById (String uuid) {

        return retryTemplate.execute( ret -> {
            log.info("Consultando redis");
            return repository.findById(uuid);
        } );

    }
}
