package io.github.tlsdla1235.seniormealplan.domain.report;


import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("WEEKLY") // report_type이 'WEEKLY'인 경우
public class WeeklyReport extends Report {

    @Column(name = "week_start")
    private LocalDate weekStart;

    @Column(name = "week_end")
    private LocalDate weekEnd;

    @Column(name = "avg_kcal")
    private BigDecimal avgKcal;

    @Lob
    @Column(name = "summary_good_point")
    private String summaryGoodPoint;

    @Lob
    @Column(name = "summary_bad_point")
    private String summaryBadPoint;

    @Lob
    @Column(name = "summary_ai_recommand")
    private String summaryAiRecommand;

    @Enumerated(EnumType.STRING)
    private Severity severity;

//    @Column(name = "summary_score")
//    private Integer summaryScore;

    // 'something_to_graph' 컬럼은 아직 추가 x 어떤 느낌인지 논의를 더 해보고 구현하겠습니다.
}