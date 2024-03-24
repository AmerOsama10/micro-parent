package com.api.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.csrf(t -> t.disable());

		http.authorizeExchange(
				exchange -> exchange.pathMatchers("/eureka/**").permitAll()
				.anyExchange().authenticated());
		
		http.oauth2ResourceServer(t -> {
			t.jwt(Customizer.withDefaults());
		});

		return http.build();
	}

}
