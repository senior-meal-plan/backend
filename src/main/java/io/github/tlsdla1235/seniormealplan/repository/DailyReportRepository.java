package io.github.tlsdla1235.seniormealplan.repository;

import io.github.tlsdla1235.seniormealplan.domain.report.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
}
