package dev.hust.simpleasia.core.service;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.port.MinioPort;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MinioAdapter implements MinioPort {
    private final MinioClient minioClient;
    private final MinioService minioService;
    private final String bucketName;

    public MinioAdapter(MinioClient minioClient, MinioService minioService, String bucketName) {
        this.minioClient = minioClient;
        this.minioService = minioService;
        this.bucketName = bucketName;
    }

    @Override
    public String getObjectUrl(String objectName) throws InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException {
        return minioClient.getObjectUrl(bucketName, objectName);
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException, MinioException {
        Path path = Path.of(Objects.requireNonNull(file.getOriginalFilename()));
        minioService.upload(path, file.getInputStream(), file.getContentType());
    }

    @Override
    public void uploadFileWithPrefixPath(MultipartFile file, String prefixPath) {
        try {
            String objectName = prefixPath + Objects.requireNonNull(file.getOriginalFilename());

            minioClient.putObject(bucketName, objectName.replace("\\", "/"), file.getInputStream(), file.getSize() - 1, file.getContentType());
        } catch (Exception ex) {
            throw new BusinessException("Something went wrong while uploading the file", ex);
        }
    }
}
