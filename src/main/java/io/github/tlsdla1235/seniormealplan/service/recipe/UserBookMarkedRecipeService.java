package io.github.tlsdla1235.seniormealplan.service.recipe;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.recipe.Recipe;
import io.github.tlsdla1235.seniormealplan.domain.recipe.UserBookmarkedRecipe;
import io.github.tlsdla1235.seniormealplan.repository.recipe.RecipeRepository;
import io.github.tlsdla1235.seniormealplan.repository.recipe.UserBookmarkRecipeRepository;
import io.github.tlsdla1235.seniormealplan.repository.recipe.UserWeeklyRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserBookMarkedRecipeService {
    private final RecipeRepository recipeRepository;
    private final UserBookmarkRecipeRepository userBookmarkRecipeRepository;

    @Transactional
    public void addUserBookmarkRecipe(User user, Recipe recipe)
    {
        if (userBookmarkRecipeRepository.findByUserAndRecipe(user, recipe).isPresent()) {
            log.warn("사용자 id: {}가 이미 북마크한 레시피(id:{})를 다시 북마크하려 했습니다.", user.getUserId(), recipe.getRecipeId());
            return;
        }

        UserBookmarkedRecipe userBookmarkedRecipes = UserBookmarkedRecipe.builder()
                .user(user)
                .recipe(recipe)
                .build();

        log.info("사용자 id: {}에 대한 레시피 북마크 추가. recipeId: {}", user.getUserId(), recipe.getRecipeId());
        userBookmarkRecipeRepository.save(userBookmarkedRecipes);
    }

    public List<Recipe> findUserBookmarkRecipeByUser(User user)
    {
        return userBookmarkRecipeRepository.findByUser(user).stream().map(UserBookmarkedRecipe::getRecipe).toList();
    }

}
