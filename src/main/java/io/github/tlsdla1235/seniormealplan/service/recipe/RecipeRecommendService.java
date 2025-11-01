package io.github.tlsdla1235.seniormealplan.service.recipe;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.recipe.Recipe;
import io.github.tlsdla1235.seniormealplan.domain.recipe.UserWeeklyRecommendation;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.AnalysisResultDto.WeeklyAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.repository.recipe.RecipeRepository;
import io.github.tlsdla1235.seniormealplan.repository.recipe.UserWeeklyRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeRecommendService {
    private final UserWeeklyRecommendationRepository userWeeklyRecommendationRepository;
    private final RecipeRepository recipeRepository;

    public List<UserWeeklyRecommendation> createWeeklyRecommendations(User user, WeeklyAnalysisResultDto analysisResult) {
        LocalDate generatedDate = LocalDate.now();
        List<UserWeeklyRecommendation> recommendations = analysisResult.aiRecommendRecipe().stream()
                .map(recipeId -> {
                    Recipe recipeReference = recipeRepository.getReferenceById(recipeId);
                    return new UserWeeklyRecommendation(user, recipeReference, generatedDate);
                })
                .collect(Collectors.toList());
        return userWeeklyRecommendationRepository.saveAll(recommendations);
    }
}
