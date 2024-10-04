package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yalli.wah.service.MinioService;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
@CrossOrigin
public class FileController {
    private final MinioService minioService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String upload(
            @RequestPart("file") MultipartFile file) throws Exception {
        return minioService.uploadFile(file);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) throws Exception {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(minioService.downloadFile(fileName)));
    }
}
