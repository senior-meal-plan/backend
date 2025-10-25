package io.github.tlsdla1235.seniormealplan.service.orchestration;

import com.amazonaws.services.s3.AmazonS3Client;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.report.MealReport;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealRequestDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalysisMealResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealCreateRequest;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlResponse;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import io.github.tlsdla1235.seniormealplan.service.admin.S3UploadService;
import io.github.tlsdla1235.seniormealplan.service.food.FoodService;
import io.github.tlsdla1235.seniormealplan.service.report.MealReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadMealService {
    private final S3UploadService s3UploadService;
    private final MealRepository mealRepository;
    private final MealReportService mealReportService;
    private final WebClient webClient;
    private final FoodService foodService;
    @Value("${service.analysis.url}")
    private String analysisApiUrl; // FastAPI 서버 주소 (application.yml)

    @Value("${service.webhook.callback-url}")
    private String webhookCallbackUrl; // 우리 웹훅 주소 (application.yml)


    public PresignedUrlResponse generatePresignedUrl(String originalFileName){
        return s3UploadService.generatePresignedUrl(originalFileName);
    }

    @Transactional
    public Meal saveMeal(Meal meal, String uniqueFileName){
        String url = s3UploadService.getFileUrl(uniqueFileName);
        meal.setPhotoUrl(url);
        mealRepository.save(meal);
        log.info("사용자 id {}에 대한 meal이 생성되었습니다. mealid ={}", meal.getUser().getUserId(), meal.getMealId());
        MealReport mealReport = mealReportService.createPendingMealReport(meal);
        log.info("사용자 id {}에 대한 mealReport가 생성되었습니다. reportid ={}", meal.getUser().getUserId(), mealReport.getReportId());
//        requestMealAnalysis(meal);
        return meal;
    }

    @Async
    public void requestMealAnalysis(Meal meal) {
        AnalysisMealRequestDto requestDto = new AnalysisMealRequestDto(
                meal.getMealId(),
                meal.getPhotoUrl(),
                webhookCallbackUrl
        );

        // WebClient를 사용해 FastAPI 서버에 분석 요청
        webClient.post()
                .uri(analysisApiUrl)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(Void.class) // 응답은 딱히 필요 없으므로 Void
                .doOnError(error -> log.error("Failed to request meal analysis", error))
                .subscribe();
    }

    @Transactional
    public void updateMealWithAnalysis(AnalysisMealResultDto analysisMealResultDto) {
        Meal meal = mealRepository.findById(analysisMealResultDto.mealId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 식사 데이터를 찾을 수 없습니다: " + analysisMealResultDto.mealId()));
        meal.setTotalKcal(analysisMealResultDto.totalKcal());
        meal.setTotalCalcium(analysisMealResultDto.totalCalcium());
        meal.setTotalCarbs(analysisMealResultDto.totalCarbs());
        meal.setTotalProtein(analysisMealResultDto.totalProtein());
        meal.setTotalFat(analysisMealResultDto.totalFat());

        mealReportService.updateMealReportWithAnalysis(analysisMealResultDto);
        foodService.createFoodsFromAnalysisAndLinkToMeal(analysisMealResultDto);

        return;
    }
}
