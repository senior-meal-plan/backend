package io.github.tlsdla1235.seniormealplan.dto.dailyreport;

import io.github.tlsdla1235.seniormealplan.domain.report.DailyReport;

import java.math.BigDecimal;

public record DailyReportResponseDto(
        BigDecimal summarize_score,
        String severity,
        String summary
) {
    public static DailyReportResponseDto fromDailyReport(DailyReport dailyReport)
    {
        return new DailyReportResponseDto(dailyReport.getSummarizeScore(), dailyReport.getSeverity().name(),dailyReport.getSummary());
    }
}
