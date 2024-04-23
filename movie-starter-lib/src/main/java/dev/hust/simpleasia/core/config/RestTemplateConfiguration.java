package dev.hust.simpleasia.core.config;

import dev.hust.simpleasia.core.service.RestTemplateClientAdapter;
import dev.hust.simpleasia.port.RestTemplateClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(RestTemplateClient.class)
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateClient restTemplateClient() {
        return new RestTemplateClientAdapter(restTemplate());
    }
}
