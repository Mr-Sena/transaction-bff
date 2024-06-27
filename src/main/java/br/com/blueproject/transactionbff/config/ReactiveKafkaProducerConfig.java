package br.com.blueproject.transactionbff.config;

import br.com.blueproject.transactionbff.dto.RequestTransactionDto;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaConsumerTemplate(final KafkaProperties properties) {

        Map<String, Object> props = properties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<String, RequestTransactionDto>(SenderOptions.create(props));
    }
}
