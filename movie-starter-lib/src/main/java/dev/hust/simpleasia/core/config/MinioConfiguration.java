package dev.hust.simpleasia.core.config;

import com.jlefebure.spring.boot.minio.MinioService;
import dev.hust.simpleasia.core.entity.MinioProperties;
import dev.hust.simpleasia.core.service.MinioAdapter;
import dev.hust.simpleasia.port.MinioPort;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MinioPort.class)
@ConditionalOnProperty(
        value = "app.minio.enabled",
        havingValue = "true"
)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioService minioService;
    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioPort minioPort() {
        return new MinioAdapter(minioClient, minioService, minioProperties.getBucketName());
    }

}
