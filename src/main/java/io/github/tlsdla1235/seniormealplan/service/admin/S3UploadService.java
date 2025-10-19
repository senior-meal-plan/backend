package io.github.tlsdla1235.seniormealplan.service.admin;


import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 테스트용 텍스트 파일을 업로드합니다.
     * @param fileName S3에 저장될 파일 이름
     * @param content 파일에 들어갈 내용
     * @return 업로드된 파일의 URL
     */
    public String uploadTestFile(String fileName, String content) {
        try {
            // 업로드할 파일의 내용을 InputStream으로 변환합니다.
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);

            // S3에 업로드하기 위한 메타데이터를 설정합니다. (여기서는 간단히 생략)
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setContentLength(contentBytes.length);

            // S3에 파일을 업로드합니다.
            log.info("Uploading file to S3 bucket: {}", bucket);
            amazonS3Client.putObject(bucket, fileName, inputStream, null);
            log.info("File upload complete: {}", fileName);

            // 업로드된 파일의 URL을 반환합니다.
            return amazonS3Client.getUrl(bucket, fileName).toString();

        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RuntimeException("S3 file upload failed", e);
        }
    }
}
