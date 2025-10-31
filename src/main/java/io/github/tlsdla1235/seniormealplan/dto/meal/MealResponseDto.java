package io.github.tlsdla1235.seniormealplan.dto.meal;

import io.github.tlsdla1235.seniormealplan.domain.Food;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.MealType;

import java.util.List;

//User에게 MealReport를 보여주기 위해
public record MealResponseDto(
        Long mealId,
        String photoUrl,
        MealType mealType, // 아침(BREAKFAST), 점심(LUNCH), 저녁(DINNER) 구분
        List<String> foodNames // 해당 끼니에 포함된 음식 이름 목록
) {
    public static MealResponseDto from(Meal meal) {
        List<String> foodNames = meal.getFoods().stream()
                .map(Food::getName)
                .toList();

        return new MealResponseDto(
                meal.getMealId(),
                meal.getPhotoUrl(),
                meal.getMealType(),
                foodNames
        );
    }
}
