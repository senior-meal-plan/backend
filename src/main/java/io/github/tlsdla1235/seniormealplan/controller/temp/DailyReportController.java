package io.github.tlsdla1235.seniormealplan.controller.temp;

import io.github.tlsdla1235.seniormealplan.config.JwtAuthFilter;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.service.orchestration.GenerateDailyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temp/reports")
public class DailyReportController {
    private final GenerateDailyReportService generateDailyReportService;

    @PostMapping("/me/daily")
    public ResponseEntity<String> generateMyDailyReport(
            @AuthenticationPrincipal JwtAuthFilter.JwtPrincipal me,
            @RequestBody(required = false) LocalDate date) {

        // 1. 인증 정보 확인 (토큰이 없거나 유효하지 않으면 me는 null)
        if (me == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }
        User u = new User();
        u.setUserId(me.userId());
        generateDailyReportService.generateReportAndRequestAnalysis(u, date);

        return ResponseEntity.accepted().body(
                "데일리 리포트 생성 요청이 접수되었습니다. User ID: " + u.getUserId() + ", Date: " + date
        );
    }



}
