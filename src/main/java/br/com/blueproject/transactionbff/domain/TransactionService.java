package br.com.blueproject.transactionbff.domain;

import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import br.com.blueproject.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
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
    private ReactiveKafkaProducerTemplate<String, RequestTransactionDto> kafkaProducerTemplate;

    @Value("${app.topic}")
    private String topicName;


    public TransactionService(TransactionRedisRepository repository, RetryTemplate retryTemplate, ReactiveKafkaProducerTemplate<String, RequestTransactionDto> kafkaProducerTemplate) {
        this.repository = repository;
        this.retryTemplate = retryTemplate;
        this.kafkaProducerTemplate = kafkaProducerTemplate;
    }

    // The 'value' argument is the problem failure that trigger the retry sequence.
    @Retryable( value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    // QueryTimeoutException -> When lost redis database connection.
    @Transactional
    public Optional<TransactionDto> save (final RequestTransactionDto dadosDeTransferencia) {
        dadosDeTransferencia.setData(LocalDateTime.now());

        kafkaProducerTemplate.send(topicName, dadosDeTransferencia)
                .doOnSuccess(theResult -> log.info("Sucesso ao publicar a mensagem. \n{}", theResult.toString())).subscribe();

        return Optional.of(repository.save(dadosDeTransferencia));
    }


    public Optional<TransactionDto> findById (String uuid) {

        return retryTemplate.execute( ret -> {
            log.info("Consultando redis");
            return repository.findById(uuid);
        } );

    }
}
