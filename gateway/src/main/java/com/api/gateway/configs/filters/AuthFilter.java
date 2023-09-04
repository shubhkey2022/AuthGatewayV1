package com.api.gateway.configs.filters;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.gateway.errorHandlers.AuthException;
import com.api.gateway.services.JwtTokenUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {

	final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Value("${header.auth.name}")
	private String authHeaderName;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {

		final String token = exchange.getRequest().getHeaders().getFirst(this.authHeaderName);

		if (token == null || token.isEmpty()) {
			throw new AuthException(AuthException.Codes.EA_001);
		}

		final Long exp = jwtUtil.getExpirationDate(token);
		if (exp == 0L) {
			throw new AuthException(AuthException.Codes.EA_002);
		}

		if (exp != 0L && exp > -1) {
			final Date now = new Date();
			if (new Date(exp).before(now)) {
				// throw new AuthException(AuthException.Codes.EA_003);
			}
		}

		if (!jwtUtil.validateRole(token)) {
			throw new AuthException(AuthException.Codes.EA_004);
		}

		if (!jwtUtil.validateDisplayName(token)) {
			throw new AuthException(AuthException.Codes.EA_004);
		}

		final ServerHttpRequest request = exchange.getRequest().mutate().build();
		final ServerWebExchange exchange1 = exchange.mutate().request(request).build();
		return chain.filter(exchange1);
	}

}
