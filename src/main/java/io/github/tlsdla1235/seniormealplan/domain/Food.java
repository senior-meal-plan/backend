package io.github.tlsdla1235.seniormealplan.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "foods")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

    @Column(name = "serving_size")
    private BigDecimal servingSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    @JsonBackReference("meal-food")
    private Meal meal;
}