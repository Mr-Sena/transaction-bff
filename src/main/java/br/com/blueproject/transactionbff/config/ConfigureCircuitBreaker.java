package br.com.blueproject.transactionbff.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class ConfigureCircuitBreaker {


    @Bean
    public CircuitBreaker countCircuitBreaker() {

        CircuitBreakerConfig configure = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // Tipo de espécie em contagem.
                .slidingWindowSize(5) // For this case [COUNT_BASED] -> the last slidingWindowSize calls are recorded and aggregated.
                .slowCallRateThreshold(65.0f) // Limite em porcentagem slow calls. Quando a porcentagem ultrapassa o limite, CircuitBreaker inicia a transição.
                .slowCallDurationThreshold(Duration.ofSeconds(2)).build(); // Define the time limit for a slow call

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(configure);
        return registry.circuitBreaker("limiteSearchBasedOnCount");
    }

    @Bean
    public CircuitBreaker timeCircuitBreaker() {

        CircuitBreakerConfig configure = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .minimumNumberOfCalls(3)
                .slidingWindowSize(5) // This case, [ TIME_BASED ] , the calls of the last slidingWindowSize seconds recorded and aggregated.
                .failureRateThreshold(70.0f)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .writableStackTraceEnabled(false)
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(configure);
        return registry.circuitBreaker("limiteSearchBasedOnTime");
    }
}
