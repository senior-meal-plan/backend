package io.github.tlsdla1235.seniormealplan.service.report;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import io.github.tlsdla1235.seniormealplan.domain.report.WeeklyReport;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.AnalysisResultDto.AnalysisWeeklyReportDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.AnalysisResultDto.WeeklyAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.GetWeeklyReportDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.SimpleWeeklyReportDto;
import io.github.tlsdla1235.seniormealplan.repository.WeeklyReportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeeklyReportService {
    private final WeeklyReportRepository weeklyReportRepository;


    public WeeklyReport createPendingWeeklyReport(User user, LocalDate dateForWeek) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("User is not valid.");
        }
        if (dateForWeek == null) {
            throw new IllegalArgumentException("Date is not valid.");
        }

        LocalDate weekStart = dateForWeek.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = dateForWeek.with(DayOfWeek.SUNDAY);

        WeeklyReport weeklyReport = new WeeklyReport(user, weekStart, weekEnd);

        WeeklyReport savedReport = weeklyReportRepository.save(weeklyReport);

        log.info("Weekly Report created in PENDING state. ID: {}, UserID: {}, Week: {} ~ {}",
                savedReport.getReportId(), user.getUserId(), weekStart, weekEnd);
        return savedReport;
    }

    public void updatePendingWeeklyReport(WeeklyAnalysisResultDto resultDto) {
        AnalysisWeeklyReportDto updateContent =  resultDto.weeklyReport();
        log.info("report Id:{}에 대한 주간 리포트 업데이트를 진행합니다.", updateContent.WeeklyReportId());
        WeeklyReport weeklyReport = weeklyReportRepository.findById(updateContent.WeeklyReportId()).orElseThrow();
        Severity severity = Severity.valueOf(updateContent.severity().toUpperCase());
        weeklyReport.UpdateWithAnalysis(updateContent.summaryGoodPoint(), updateContent.summaryBadPoint(),
                                        updateContent.summaryAiRecommend(), severity);
        log.info("report id:{}에 대한 주간 리포트 업데이트가 완료 되었습니다.",weeklyReport.getReportId());
    }

    /**
     * 이 아래로 유저가 사용하는 조회
     *
     */
    public List<SimpleWeeklyReportDto> getAllWRDateFromUser(User user) {
        return weeklyReportRepository.findSimpleReportsByUserOrderByWeekEndDesc(user);
    }


    public GetWeeklyReportDto getReport(Long reportId) {
        WeeklyReport persist =  weeklyReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("리포트를 찾을 수 없습니다. ID: " + reportId));
        return GetWeeklyReportDto.toDto(persist);
    }

}
