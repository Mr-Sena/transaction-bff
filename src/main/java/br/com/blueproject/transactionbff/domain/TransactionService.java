package br.com.blueproject.transactionbff.domain;

import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import br.com.blueproject.transactionbff.dto.TransactionDto;
import br.com.blueproject.transactionbff.feign.TransactionClientHttp;
import br.com.blueproject.transactionbff.redis.TransactionRedisRepository;
import br.com.blueproject.transactionbff.tratamentodesvio.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRedisRepository repository;
    private final RetryTemplate retryTemplate;
    private final ReactiveKafkaProducerTemplate<String, RequestTransactionDto> kafkaProducerTemplate;
    private final TransactionClientHttp httpClient;

    @Value("${app.topic}")
    private String topicName;


    public TransactionService(TransactionRedisRepository repository, RetryTemplate retryTemplate, ReactiveKafkaProducerTemplate<String,
            RequestTransactionDto> kafkaProducerTemplate, TransactionClientHttp httpClient) {

        this.repository = repository;
        this.retryTemplate = retryTemplate;
        this.kafkaProducerTemplate = kafkaProducerTemplate;
        this.httpClient = httpClient;
    }

    // The 'value' argument is the problem failure that trigger the retry sequence.
    @Retryable( value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000))
    // QueryTimeoutException -> When lost redis database connection.
    @Transactional
    public Mono<RequestTransactionDto> save (final RequestTransactionDto dadosDeTransferencia) {

        // Mono pipeline sequence [ Failure | Success | Finally ]
        return Mono.fromCallable(() -> {

            dadosDeTransferencia.setData(LocalDateTime.now());
            dadosDeTransferencia.naoAnalisada();

            return repository.save(dadosDeTransferencia);
        }).doOnError(throwable -> {

            log.error(throwable.getMessage(), throwable);
            throw new NotFoundException("Unable to find resource.");
        })

        .doOnSuccess(transferencia -> {

            log.info("Persistência de dados realizada com sucesso: {}", transferencia);
            kafkaProducerTemplate.send(topicName, dadosDeTransferencia)
                    .doOnSuccess(theResult -> log.info(theResult.toString())).subscribe();

        })
        .doFinally(signalType -> {

            if (signalType.compareTo(SignalType.ON_COMPLETE) == 0) {
                log.info("Mensagem enviada com sucesso para o Kafka. {}", dadosDeTransferencia);
            }
        });
    }



    public Flux<List<TransactionDto>> findContaStream(final Long agencia, final Long conta) {

        List<TransactionDto> queryResult = findByAgenciaAndConta(agencia, conta);
        return Flux.fromIterable(queryResult).cache(Duration.ofSeconds(2)) // Persistência de memória em cache durante 2 segundos.
                .limitRate(200).defaultIfEmpty(new TransactionDto()) // LimitRate is the items amount for this list.
                .buffer(200);
    }


    public List<TransactionDto> findByAgenciaAndConta(final Long agencia, final Long conta) {

        return httpClient.buscarTransferencias(agencia, conta);
    }

    public Optional<TransactionDto> findById (String uuid) {

        return retryTemplate.execute( ret -> {
            log.info("Consultando redis");
            return repository.findById(uuid);
        } );

    }
}
