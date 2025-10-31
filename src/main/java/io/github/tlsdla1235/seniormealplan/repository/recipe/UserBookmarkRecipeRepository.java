package io.github.tlsdla1235.seniormealplan.repository.recipe;

import io.github.tlsdla1235.seniormealplan.domain.recipe.UserBookmarkedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookmarkRecipeRepository extends JpaRepository<UserBookmarkedRecipe, Long> {
}
