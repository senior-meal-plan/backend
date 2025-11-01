package io.github.tlsdla1235.seniormealplan.dto.meal;

import io.github.tlsdla1235.seniormealplan.domain.Meal;

import java.time.LocalDate;

public record MealImageDto(
        Long mealId,
        String photoUrl,
        LocalDate date
) {
    public static MealImageDto fromMeal(Meal meal) {
        return new MealImageDto(
                meal.getMealId(),
                meal.getPhotoUrl(),
                meal.getMealDate()
        );
    }
}
