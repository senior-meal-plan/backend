package io.github.tlsdla1235.seniormealplan.domain.report;

import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

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
}