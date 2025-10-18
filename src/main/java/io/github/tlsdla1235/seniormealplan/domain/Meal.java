package io.github.tlsdla1235.seniormealplan.domain;

import io.github.tlsdla1235.seniormealplan.domain.enumPackage.MealType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
@Getter
@NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Integer mealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealFood> mealFoods = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Column(name = "meal_date")
    private LocalDate mealDate;

    @Column(name = "meal_time")
    private LocalTime mealTime;

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

    @Lob // 글자 수 제한이 없는 TEXT 타입과 매핑
    @Column(name = "memo")
    private String memo;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}