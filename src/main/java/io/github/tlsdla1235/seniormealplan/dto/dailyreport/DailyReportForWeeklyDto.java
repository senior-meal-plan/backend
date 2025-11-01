package io.github.tlsdla1235.seniormealplan.dto.dailyreport;

import io.github.tlsdla1235.seniormealplan.domain.report.DailyReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyReportForWeeklyDto(
        Long ReportId,
        LocalDate date,
        BigDecimal Score
) {
    public static DailyReportForWeeklyDto from(DailyReport dailyReport) {
        return new DailyReportForWeeklyDto(
                dailyReport.getReportId(),
                dailyReport.getReportDate(),
                dailyReport.getSummarizeScore()
        );
    }
}
