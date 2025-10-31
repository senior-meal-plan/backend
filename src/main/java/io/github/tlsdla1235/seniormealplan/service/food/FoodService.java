package io.github.tlsdla1235.seniormealplan.service.food;

import io.github.tlsdla1235.seniormealplan.domain.Food;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalyzedFoodDto;
import io.github.tlsdla1235.seniormealplan.repository.FoodRepository;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {
    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;


    public void createFoodsFromAnalysisAndLinkToMeal(AnalysisMealResultDto resultDto) {
        Meal meal = mealRepository.findById(resultDto.mealId())
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + resultDto.mealId()));

        // 재분석에 대비하여, 기존에 연결된 Food 목록을 모두 제거합니다.
        meal.getFoods().clear();

        if (resultDto.foods() == null || resultDto.foods().isEmpty()) {
            log.warn("No food items found in analysis result for Meal ID: {}", resultDto.mealId());
            return;
        }

        for (AnalyzedFoodDto foodDto : resultDto.foods()) {
            Food newFood = Food.builder()
                    .meal(meal) // 연관관계 설정
                    .name(foodDto.name())
                    .kcal(foodDto.kcal())
                    .protein(foodDto.protein())
                    .carbs(foodDto.carbs())
                    .fat(foodDto.fat())
                    .calcium(foodDto.calcium())
                    .servingSize(foodDto.servingSize())
                    .saturatedFatPercentKcal(foodDto.saturatedFatPercentKcal())
                    .unsaturatedFat(foodDto.unsaturatedFat())
                    .dietaryFiber(foodDto.dietaryFiber())
                    .sodium(foodDto.sodium())
                    .addedSugarKcal(foodDto.addedSugarKcal())
                    .processedMeatGram(foodDto.processedMeatGram())
                    .vitaminD_IU(foodDto.vitaminD_IU())
                    .isVegetable(foodDto.isVegetable())
                    .isFruit(foodDto.isFruit())
                    .isFried(foodDto.isFried())
                    .build();
            meal.getFoods().add(newFood);
        }
        log.info("{} food items created and linked to Meal ID: {}", meal.getFoods().size(), meal.getMealId());
    }
}
