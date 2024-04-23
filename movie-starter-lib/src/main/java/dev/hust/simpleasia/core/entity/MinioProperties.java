package dev.hust.simpleasia.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.core.minio")
@Getter
@Setter
public class MinioProperties {
    private String bucketName = "movie-system";
    private String url;
    private String accessKey;
    private String secretKey;
}
