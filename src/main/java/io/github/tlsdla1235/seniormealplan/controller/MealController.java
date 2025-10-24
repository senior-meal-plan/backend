package io.github.tlsdla1235.seniormealplan.controller;

import io.github.tlsdla1235.seniormealplan.config.JwtAuthFilter;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealCreateRequest;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlRequest;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlResponse;
import io.github.tlsdla1235.seniormealplan.service.orchestration.UploadMealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MealController {
    private final UploadMealService uploadMealService;

    @PostMapping("/v1/user/me/uploads")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(@RequestBody PresignedUrlRequest request)
    {
        PresignedUrlResponse response = uploadMealService.generatePresignedUrl(request.fileName());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/user/me/meal-reports")
    public ResponseEntity<String> generateMealReport(@RequestBody MealCreateRequest newMeal, @AuthenticationPrincipal JwtAuthFilter.JwtPrincipal me)
    {
        Meal meal = Meal.builder()
                .mealDate(newMeal.mealDate())
                .mealTime(newMeal.mealTime())
                .user(User.builder().userId(me.userId()).build())
                .memo(newMeal.memo())
                .mealType(newMeal.mealType())
                .build();
        uploadMealService.saveMeal(meal, newMeal.uniqueFileName());

        return ResponseEntity.ok().body("Meal report generated");
    }
}
