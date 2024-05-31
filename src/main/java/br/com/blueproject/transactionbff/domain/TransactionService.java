package br.com.blueproject.transactionbff.domain;

import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import br.com.blueproject.transactionbff.redis.TransactionRedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    private TransactionRedisRepository repository;

    public TransactionService(TransactionRedisRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Optional<TransactionDto> save (final RequestTransactionDto requestDto) {

        requestDto.setData(LocalDateTime.now());
        return Optional.of(repository.save(requestDto));
    }

    public Optional<TransactionDto> findById (String uuid) {

        return repository.findById(uuid);
    }
}
