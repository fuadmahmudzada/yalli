package org.yalli.wah.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.name}")
    private String bucketName;

    private final static String IMAGE_URL = "yalli";

    public String uploadFile(MultipartFile stream) throws Exception {
        var imageUrl = IMAGE_URL + LocalDateTime.now();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(imageUrl)
                        .stream(stream.getInputStream(), stream.getSize(), -1)
                        .contentType("application/octet-stream")
                        .build()).bucket();
        return imageUrl;
    }

    public InputStreamResource downloadFile(String objectName) throws Exception {
        return new InputStreamResource(minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build())
        );
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
