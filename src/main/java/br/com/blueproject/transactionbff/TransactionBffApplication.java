package br.com.blueproject.transactionbff;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRedisRepositories(basePackages = {"br.com.blueproject.transactionbff.redis"})
@EnableFeignClients
@EnableRetry
public class TransactionBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionBffApplication.class, args);
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> metrisCommonsTags() {
		return registry -> registry.config().commonTags("application", "transaction-bff");
	}

}
