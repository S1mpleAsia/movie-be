package dev.hust.simpleasia.port;

import com.jlefebure.spring.boot.minio.MinioException;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioPort {
    String getObjectUrl(String objectName) throws InvalidBucketNameException, InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException;
    void uploadFile(MultipartFile file) throws IOException, MinioException;
    void uploadFileWithPrefixPath(MultipartFile file, String prefixPath) throws IOException, MinioException;
}
