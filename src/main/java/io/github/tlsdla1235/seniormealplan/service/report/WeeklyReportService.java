package io.github.tlsdla1235.seniormealplan.service.report;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.report.WeeklyReport;
import io.github.tlsdla1235.seniormealplan.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

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
}
