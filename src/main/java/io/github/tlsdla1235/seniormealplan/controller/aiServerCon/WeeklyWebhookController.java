package io.github.tlsdla1235.seniormealplan.controller.aiServerCon;

import com.amazonaws.Response;
import io.github.tlsdla1235.seniormealplan.config.JwtAuthFilter;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.AnalysisResultDto.WeeklyAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.WeeklyReportGenerateRequestDto;
import io.github.tlsdla1235.seniormealplan.service.orchestration.GenerateWeeklyReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks/weekly")
public class WeeklyWebhookController {
    private final GenerateWeeklyReportsService generateWeeklyReportsService;

    @PostMapping("/analysis-complete")
    public ResponseEntity<String> complete(@RequestBody WeeklyAnalysisResultDto resultDto)
    {
        generateWeeklyReportsService.updateWeeklyResult(resultDto);
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/dtoCheckApi")
    public ResponseEntity<String> check(@RequestBody WeeklyReportGenerateRequestDto requestDto)
    {
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/testCode")
    public ResponseEntity<String> test(@AuthenticationPrincipal JwtAuthFilter.JwtPrincipal me)
    {
        generateWeeklyReportsService.createRequestDto(User.builder().userId(me.userId()).build());
        return ResponseEntity.ok("Success");
    }

}
