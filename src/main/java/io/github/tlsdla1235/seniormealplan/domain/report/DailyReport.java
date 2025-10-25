package io.github.tlsdla1235.seniormealplan.domain.report;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.ReportStatus;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("DAILY") // report_type이 'DAILY'인 경우
public class DailyReport extends Report {

    @Column(name = "total_kcal")
    private BigDecimal totalKcal;

    @Column(name = "total_protein")
    private BigDecimal totalProtein;

    @Column(name = "total_carbs")
    private BigDecimal totalCarbs;

    @Column(name = "total_fat")
    private BigDecimal totalFat;

    @Column(name = "total_calcium")
    private BigDecimal totalCalcium;

    @Lob
    @Column(name = "summary")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    // 점수 필드들
    @Column(name = "pro_score")
    private BigDecimal proScore;

    @Column(name = "cal_score")
    private BigDecimal calScore;

    @Column(name = "fat_score")
    private BigDecimal fatScore;

    public DailyReport(User user, LocalDate reportDate) {
        super.setUser(user);
        super.setReportDate(reportDate);
        // status는 @PrePersist에 의해 PENDING으로 자동 설정됩니다.
    }

    public void updateWithAnalysis(BigDecimal totalKcal, BigDecimal totalProtein,
                                   BigDecimal totalCarbs, BigDecimal totalFat,
                                   BigDecimal totalCalcium, String summary,
                                   Severity severity, BigDecimal proScore,
                                   BigDecimal calScore, BigDecimal fatScore) {
        this.totalKcal = totalKcal;
        this.totalProtein = totalProtein;
        this.totalCarbs = totalCarbs;
        this.totalFat = totalFat;
        this.totalCalcium = totalCalcium;
        this.summary = summary;
        this.severity = severity;
        this.proScore = proScore;
        this.calScore = calScore;
        this.fatScore = fatScore;
        super.changeStatus(ReportStatus.COMPLETE); // 상태를 COMPLETE로 변경
    }

    /**
     * 분석 실패 시 상태를 FAILED로 변경합니다.
     */
    public void markAsFailed() {
        super.changeStatus(ReportStatus.FAILED);
    }
}