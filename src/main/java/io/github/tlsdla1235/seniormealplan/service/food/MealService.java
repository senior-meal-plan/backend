package io.github.tlsdla1235.seniormealplan.service.food;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealResponseDto;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealService {
    private final MealRepository mealRepository;


    public List<MealResponseDto> getTodayMeals(User user) {
        LocalDate today = LocalDate.now();
        List<Meal> meals = findByUserAndMealDateWithFoods(user, today);
        List<MealResponseDto> mealResponseDtos = meals.stream().map(MealResponseDto::from).toList();
        log.info("사용자 id:{}에 대한 getTodayMeals 결과값: {}", user.getUserId(), mealResponseDtos);
        return mealResponseDtos;
    }

    public List<Meal> findByUserAndMealDateWithFoods(User user, LocalDate date) {
        return mealRepository.findByUserAndMealDateWithFoods(user, date);
    }

    public void updateMealWithAnalysis(AnalysisMealResultDto analysisMealResultDto) {
        Meal meal = mealRepository.findById(analysisMealResultDto.mealId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 식사 데이터를 찾을 수 없습니다: " + analysisMealResultDto.mealId()));


        meal.setTotalKcal(analysisMealResultDto.totalKcal());
        meal.setTotalCalcium(analysisMealResultDto.totalCalcium());
        meal.setTotalCarbs(analysisMealResultDto.totalCarbs());
        meal.setTotalProtein(analysisMealResultDto.totalProtein());
        meal.setTotalFat(analysisMealResultDto.totalFat());

        meal.setDairyIntake(analysisMealResultDto.isDairyIntake());
        meal.setVitaminCIntake(analysisMealResultDto.isVitaminCIntake());
        meal.setVitaminBIntake(analysisMealResultDto.isVitaminBIntake());
        meal.setFishIntake(analysisMealResultDto.isFishIntake());
        meal.setNutsIntake(analysisMealResultDto.isNutsIntake());
        meal.setVegetableOilIntake(analysisMealResultDto.isVegetableOilIntake());
        meal.setUnrefinedCarbsIntake(analysisMealResultDto.isUnrefinedCarbsIntake());

        log.info("Meal(ID: {}) updated with analysis.", meal.getMealId());
    }
}
