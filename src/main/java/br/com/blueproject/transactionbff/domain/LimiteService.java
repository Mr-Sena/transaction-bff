package br.com.blueproject.transactionbff.domain;

import br.com.blueproject.transactionbff.dto.LimiteDiarioDto;
import br.com.blueproject.transactionbff.feign.LimiteClientHttp;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Supplier;

@Service
@Slf4j
public class LimiteService {

    LimiteClientHttp httpClient;

    @Autowired
    private CircuitBreaker countCircuitBreaker; //O nome desse atributo deve ser o mesmo nome do método escrito na classe de configurabilidade [ /config package. ]

    public LimiteService(LimiteClientHttp httpClient) {
        this.httpClient = httpClient;
    }

    public Mono<LimiteDiarioDto> buscarLimiteDiario(final Long agencia, final Long conta) {

        return buscarLimiteDiarioSupplier(agencia, conta);
    }


    private Mono<LimiteDiarioDto> buscarLimiteDiarioSupplier(final Long agencia, final Long conta) {

        var limiteDiarioSup = countCircuitBreaker.decorateSupplier(() -> httpClient.buscarLimiteDiario(agencia, conta));

        return Mono.fromSupplier(Decorators.ofSupplier(limiteDiarioSup)
                    .withCircuitBreaker(countCircuitBreaker)
                    .withFallback(Arrays.asList(CallNotPermittedException.class), //This exception is thrown when has the failure.
                            e -> this.getStaticLimit()).decorate());

    }

    private LimiteDiarioDto getStaticLimit() {

        LimiteDiarioDto defaultData = new LimiteDiarioDto();
        defaultData.setValor(BigDecimal.ZERO);
        return defaultData;
    }

}
