package io.github.tlsdla1235.seniormealplan.service.recipe;

import io.github.tlsdla1235.seniormealplan.domain.recipe.Recipe;
import io.github.tlsdla1235.seniormealplan.dto.recipe.RecipeDto;
import io.github.tlsdla1235.seniormealplan.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;




    @Transactional
    public void save(RecipeDto dto) {
        Recipe newRecipe = Recipe.builder().name(dto.name()).description(dto.description()).build();
        log.info("save recipe: {}", newRecipe);
    }
}
