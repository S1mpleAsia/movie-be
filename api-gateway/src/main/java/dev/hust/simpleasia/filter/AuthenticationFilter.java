package dev.hust.simpleasia.filter;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.port.RestTemplateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
    @Autowired
    private RestTemplateClient restTemplateClient;

    @Value("${app.internal.ip}")
    private String internalIp;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Header contain token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new BusinessException("Missing authorization header", HttpStatus.FORBIDDEN);
                }

                String authHeader = Objects.requireNonNull(
                        exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);


                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    ResponseEntity<String> response = restTemplateClient.get("http://" + internalIp + ":8081/api/auth/validate?token={token}", String.class, null, authHeader);
                    log.info("Token = [{}]", response.getBody());
                } catch (Exception e) {
                    log.info("RestTemplate retrieved failed");
                    throw new BusinessException("Unauthorized access to application");
                }
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
