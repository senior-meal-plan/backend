package io.github.tlsdla1235.seniormealplan.repository;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
