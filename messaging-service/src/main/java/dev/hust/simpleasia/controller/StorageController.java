package dev.hust.simpleasia.controller;

import com.jlefebure.spring.boot.minio.MinioException;
import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.port.MinioPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {
    private final MinioPort minioPort;

    @PostMapping
    public GeneralResponse<Object> upload(@RequestBody MultipartFile file) throws MinioException, IOException {
        minioPort.uploadFile(file);

        return GeneralResponse.success(null);
    }

    @PostMapping("movie")
    public GeneralResponse<Object> uploadMovie(@RequestBody MultipartFile file, @RequestParam("path") String path) throws MinioException, IOException {
        minioPort.uploadFileWithPrefixPath(file, path);

        return GeneralResponse.success(null);
    }
}
