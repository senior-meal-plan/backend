package io.github.tlsdla1235.seniormealplan.controller.admin;

import io.github.tlsdla1235.seniormealplan.dto.tempdto.HealthTopicCreateRequestDto;
import io.github.tlsdla1235.seniormealplan.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin") // 관리자용 API 경로 예시
@RequiredArgsConstructor
public class HealthTopicController {
    private final AdminService adminService;

    @PostMapping("/health-topics")
    public ResponseEntity<Void> createHealthTopic(@RequestBody HealthTopicCreateRequestDto requestDto) {
        adminService.createHealthTopic(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
