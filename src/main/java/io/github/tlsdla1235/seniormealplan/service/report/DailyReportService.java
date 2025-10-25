package io.github.tlsdla1235.seniormealplan.service.report;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import io.github.tlsdla1235.seniormealplan.domain.report.DailyReport;
import io.github.tlsdla1235.seniormealplan.dto.dailyreport.DailyReportAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealRequestDto;
import io.github.tlsdla1235.seniormealplan.repository.DailyReportRepository;
import io.github.tlsdla1235.seniormealplan.service.admin.S3UploadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyReportService {
    private final DailyReportRepository dailyReportRepository;


    public DailyReport createPendingDailyReport(User user, LocalDate reportDate) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("유저가 유효하지 않음");
        }
        if (reportDate == null) {
            throw new IllegalArgumentException("날짜입력이 유효하지 않음");
        }
        DailyReport dailyReport = new DailyReport(user, reportDate);
        DailyReport savedReport = dailyReportRepository.save(dailyReport);
        log.info("데일리 레포트가 pending 상태로  생성되었습니다. ID: {} 유저ID: {} 날짜 {}.",
                savedReport.getReportId(), user.getUserId(), reportDate);
        return savedReport;
    }

    public DailyReport getDailyReport(Long dailyReportId) {

        return dailyReportRepository.findById(dailyReportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 데일리 리포트를 찾을 수 없습니다: " + dailyReportId));
    }

    public void updateReportWithAnalysis(DailyReportAnalysisResultDto result) {
        // 1. DTO의 reportId로 DB에서 기존 DailyReport 엔티티를 찾습니다.
        DailyReport report = this.getDailyReport(result.reportId());

        // 2. 분석 결과가 실패인 경우, 상태를 FAILED로 변경하고 로직을 종료합니다.
        if (!"SUCCESS".equals(result.status())) {
            log.error("데일리 리포트(ID: {}) 분석 실패. 원인: {}", result.reportId(), result.errorMessage());
            report.markAsFailed();
            return;
        }

        try {
            // 3. String으로 받은 severity 값을 Enum으로 변환합니다.
            Severity severityEnum = Severity.valueOf(result.severity().toUpperCase());

            // 4. 엔티티 내부의 업데이트 메서드를 호출하여 모든 필드를 갱신하고 상태를 COMPLETE로 변경합니다.
            report.updateWithAnalysis(
                    result.totalKcal(),
                    result.totalProtein(),
                    result.totalCarbs(),
                    result.totalFat(),
                    result.totalCalcium(),
                    result.summary(),
                    severityEnum,
                    result.proScore(),
                    result.calScore(),
                    result.fatScore()
            );
            log.info("데일리 리포트(ID: {})가 분석 결과로 업데이트되었습니다. 상태: COMPLETE", report.getReportId());

        } catch (IllegalArgumentException e) {
            log.error("AI 서버로부터 유효하지 않은 Severity 값을 받았습니다: '{}'. 리포트(ID: {})를 FAILED로 처리합니다.",
                    result.severity(), report.getReportId());
            report.markAsFailed();
        } catch (Exception e) {
            log.error("데일리 리포트(ID: {}) 업데이트 중 알 수 없는 오류 발생", report.getReportId(), e);
            report.markAsFailed();
        }
    }
}
