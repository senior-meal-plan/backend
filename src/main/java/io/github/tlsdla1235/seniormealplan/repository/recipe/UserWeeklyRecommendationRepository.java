package io.github.tlsdla1235.seniormealplan.repository.recipe;

import io.github.tlsdla1235.seniormealplan.domain.recipe.UserWeeklyRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWeeklyRecommendationRepository extends JpaRepository<UserWeeklyRecommendation, Long> {
}
