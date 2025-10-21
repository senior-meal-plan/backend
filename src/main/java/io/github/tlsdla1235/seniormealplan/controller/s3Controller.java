package io.github.tlsdla1235.seniormealplan.controller;

import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlRequest;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlResponse;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.UploadCompleteRequest;
import io.github.tlsdla1235.seniormealplan.service.admin.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
@Slf4j
public class s3Controller {
    private final S3UploadService s3UploadService;

    @PostMapping("/generate-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(@RequestBody PresignedUrlRequest request) {
        PresignedUrlResponse response = s3UploadService.generatePresignedUrl(request.fileName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> uploadComplete(@RequestBody UploadCompleteRequest request) {
        String finalUrl = s3UploadService.getFileUrl(request.uniqueFileName());

        // 여기서 finalUrl을 다른 서비스(예: PostService)에 넘겨 DB에 저장하는 로직 수행
        // postService.saveImageUrl(finalUrl);

        log.info("File upload complete. Stored URL: {}", finalUrl);
        return ResponseEntity.ok(finalUrl);
    }
}
