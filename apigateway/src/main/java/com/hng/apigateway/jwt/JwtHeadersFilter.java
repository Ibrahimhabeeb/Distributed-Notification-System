package com.hng.apigateway.jwt;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtHeadersFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/api/v1/auth")) {
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap(auth -> {
                    Jwt jwt = auth.getToken();
                    String userId = jwt.getClaim("userId");
                    String email = jwt.getSubject();

                    exchange.getRequest().mutate()
                            .header("X-Gateway-Verified", "true")
                            .header("X-User-Id", userId)
                            .header("X-User-Email", email)
                            .build();

                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
