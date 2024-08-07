package br.com.blueproject.transactionbff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfigBean {

    public static final String BLUE_PROJECT_ROLE = "SCOPE_BlueProjectRole";

    @Bean
    public SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http) {

        http.exceptionHandling()
            .and().redirectToHttps()
            .and().csrf().disable().formLogin().disable()
            .httpBasic().disable().authorizeExchange()
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers("/transaction/**", "/limites/**").access(

                (authentication, object) -> authentication.flatMap(auth ->

                    Mono.just(
                        new AuthorizationDecision(
                            ((JwtAuthenticationToken) auth).getAuthorities().contains(
                                new SimpleGrantedAuthority(BLUE_PROJECT_ROLE)
                            )
                        )
                    )
                )
            ).anyExchange().authenticated().and().oauth2ResourceServer().jwt();
        return http.build();
    }
}
