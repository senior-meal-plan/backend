package io.github.tlsdla1235.seniormealplan.service.food;

import io.github.tlsdla1235.seniormealplan.domain.Food;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalyzedFoodDto;
import io.github.tlsdla1235.seniormealplan.repository.FoodRepository;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;

    @Transactional
    public void createFoodsFromAnalysisAndLinkToMeal(AnalysisMealResultDto resultDto) {
        // 1. DTO에서 mealId로 Meal 엔티티를 조회합니다.
        Meal meal = mealRepository.findById(resultDto.mealId())
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + resultDto.mealId()));

        // 2. 재분석에 대비하여, 기존에 연결된 Food 목록을 모두 제거합니다.
        meal.getFoods().clear();

        // 3. DTO에 포함된 음식 목록(List<AnalyzedFoodDto>)을 순회합니다.
        for (AnalyzedFoodDto foodDto : resultDto.foods()) {

            // 4. AnalyzedFoodDto로부터 새로운 Food 엔티티를 생성합니다.
            Food newFood = Food.builder()
                    .name(foodDto.name())
                    .kcal(foodDto.kcal())
                    .protein(foodDto.protein())
                    .carbs(foodDto.carbs())
                    .fat(foodDto.fat())
                    .calcium(foodDto.calcium())
                    .servingSize(foodDto.servingSize())
                    .meal(meal)
                    .build();
            meal.getFoods().add(newFood);
        }
    }
}
