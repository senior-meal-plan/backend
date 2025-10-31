package io.github.tlsdla1235.seniormealplan.repository.recipe;

import io.github.tlsdla1235.seniormealplan.domain.recipe.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
}
