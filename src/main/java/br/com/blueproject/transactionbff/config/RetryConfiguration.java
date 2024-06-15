package br.com.blueproject.transactionbff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetryConfiguration {

    /// Essa classe permite definir uma regra geral para determinar o mecanismo de retry.

    @Bean
    public RetryTemplate retryTemplate() {

        var retryTemplate = new RetryTemplate();

        var policies = new FixedBackOffPolicy();
        policies.setBackOffPeriod(2000L);

        var simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(5);

        retryTemplate.setBackOffPolicy(policies);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }
}
