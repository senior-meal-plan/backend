package io.github.tlsdla1235.seniormealplan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "foods")
@Getter
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Integer foodId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "kcal")
    private BigDecimal kcal;

    @Column(name = "protein")
    private BigDecimal protein;

    @Column(name = "carbs")
    private BigDecimal carbs;

    @Column(name = "fat")
    private BigDecimal fat;

    @Column(name = "calcium")
    private BigDecimal calcium;
}