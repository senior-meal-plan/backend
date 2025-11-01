package io.github.tlsdla1235.seniormealplan.controller.aiServerCon;

import io.github.tlsdla1235.seniormealplan.dto.dailyreport.DailyReportAnalysisRequestDto;
import io.github.tlsdla1235.seniormealplan.dto.dailyreport.DailyReportAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.service.orchestration.GenerateDailyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks/daily/reports")
public class DailyWebHookController {
    private final GenerateDailyReportService generateDailyReportService;

    @PostMapping("/daily-analysis-complete") // 2. 데일리 리포트 전용 세부 경로
    public ResponseEntity<Void> receiveDailyReportResult(@RequestBody DailyReportAnalysisResultDto resultDto) {
        log.info("웹훅 수신: 데일리 리포트 분석 결과 (Report ID: {})", resultDto.reportId());
        generateDailyReportService.updateDailyReportWithAnalysis(resultDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dtoCheckApi")
    public ResponseEntity<String> check(@RequestBody DailyReportAnalysisRequestDto dto){
        return ResponseEntity.ok().body("good");
    }

}
