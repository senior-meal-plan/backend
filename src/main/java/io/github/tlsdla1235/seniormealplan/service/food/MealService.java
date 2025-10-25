package io.github.tlsdla1235.seniormealplan.service.food;

import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;

    public List<Meal> findByUserAndMealDateWithFoods(User user, LocalDate date) {
        return mealRepository.findByUserAndMealDateWithFoods(user, date);
    }
}
