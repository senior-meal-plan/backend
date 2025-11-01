package io.github.tlsdla1235.seniormealplan.service.food;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealImageDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealResponseDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.MealForWeeklyDto;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<MealResponseDto> getMealsByDate(User user, LocalDate date) {
        List<Meal> meals = findByUserAndMealDateWithFoods(user, date);
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

    public List<LocalDate> getAllMealDateFromUser(User user) {
        List<LocalDate> mealDates = mealRepository.findDistinctMealDatesByUser(user);
        log.info("userid:{}에 대한 getAllMealDateFromUser 반환 결과 : {}", user.getUserId(), mealDates);
        return mealDates;
    }

    public List<MealForWeeklyDto> getMealsForLastWeek(User user, LocalDate date) {
        // 1. 지난주의 월요일과 일요일 날짜 계산
        LocalDate lastMonday = date.with(DayOfWeek.MONDAY);
        LocalDate lastSunday = date.with(DayOfWeek.SUNDAY);


        // 2. Repository를 통해 해당 기간의 식사 기록을 조회
        // (MealRepository에 해당 메서드가 있다고 가정)
        List<Meal> meals = mealRepository.findByUserAndMealDateBetweenWithFoods(user, lastMonday, lastSunday);

        // 3. DTO 리스트로 변환하여 반환 (MealForWeeklyDto::fromMeal 사용)
        List<MealForWeeklyDto> mealDtos = meals.stream()
                .map(MealForWeeklyDto::fromMeal) // MealResponseDto::from -> MealForWeeklyDto::fromMeal
                .collect(Collectors.toList());

        log.info("사용자 id:{}에 대한 지난주 식사(getMealsForLastWeek) 결과값: {}", user.getUserId(), mealDtos);
        return mealDtos;
    }

    public List<MealImageDto> findByUserAndMealDateIn(User user, Collection<LocalDate> dates)
    {
        List<Meal> meals = mealRepository.findByUserAndMealDateIn(user, dates);
        return meals.stream().map(MealImageDto::fromMeal).toList();
    }
}
