package io.github.tlsdla1235.seniormealplan.repository;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalyzedFoodDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.foods WHERE m.user = :user AND m.mealDate = :date")
    List<Meal> findByUserAndMealDateWithFoods(@Param("user") User user, @Param("date") LocalDate date);
}
