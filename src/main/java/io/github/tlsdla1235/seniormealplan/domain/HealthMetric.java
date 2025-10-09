package io.github.tlsdla1235.seniormealplan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_metrics")
@Getter
@Setter
@NoArgsConstructor
public class HealthMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer metricId;

    @OneToOne(fetch = FetchType.LAZY) // User(1) : HealthMetric(1) 관계
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bmi")
    private BigDecimal bmi;

    @Column(name = "daily_kcal")
    private BigDecimal dailyKcal;

    @Column(name = "daily_protein")
    private BigDecimal dailyProtein;

    @Column(name = "daily_fat")
    private BigDecimal dailyFat;

    @Column(name = "daily_calcium")
    private BigDecimal dailyCalcium;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @PrePersist
    public void prePersist() {
        this.calculatedAt = LocalDateTime.now();
    }
}