package io.github.tlsdla1235.seniormealplan.service.orchestration;


import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.enumPackage.Severity;
import io.github.tlsdla1235.seniormealplan.domain.report.DailyReport;
import io.github.tlsdla1235.seniormealplan.dto.dailyreport.DailyReportAnalysisRequestDto;
import io.github.tlsdla1235.seniormealplan.dto.dailyreport.DailyReportAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.AnalyzedFoodDto;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealDto;
import io.github.tlsdla1235.seniormealplan.repository.DailyReportRepository;
import io.github.tlsdla1235.seniormealplan.service.admin.S3UploadService;
import io.github.tlsdla1235.seniormealplan.service.food.MealService;
import io.github.tlsdla1235.seniormealplan.service.report.DailyReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateDailyReportService {

    private final S3UploadService s3UploadService;
    private final WebClient webClient;
    private final DailyReportService dailyReportService;
    private final MealService mealService;
    private final DailyReportRepository dailyReportRepository;

    @Value("${service.daily.analysis.url}")
    private String analysisApiUrl; // FastAPI 서버 주소 (application.yml)

    @Value("${service.webhook.callback-url}")
    private String webhookCallbackUrl; // 우리 웹훅 주소 (application.yml)


    @Transactional
    public void generateReportAndRequestAnalysis(User user, LocalDate date) {
        List<Meal> meals = mealService.findByUserAndMealDateWithFoods(user, date);
        // 식사 기록이 없으면 리포트를 생성하지 않습니다.
        if (meals.isEmpty()) {
            log.info("유저 ID: {}에 대한 식사기록이 없스빈다. {}에 대한 데일리 리포트 생성을 하지 않습니다", user.getUserId(), date);
            return;
        }
        DailyReport report = dailyReportService.createPendingDailyReport(user, date);
        user.setLastDailyReportDate(date);
        log.info("Updated last daily report date for user ID: {} to {}", user.getUserId(), date);
        // 4. 외부 API에 분석을 요청하는 비동기 메서드를 호출합니다.
        this.requestAnalysisToExternalApi(user, meals, report);
    }

    @Async
    public void requestAnalysisToExternalApi(User user, List<Meal> meals, DailyReport report) {
        log.info("Starting async request for daily report analysis. Report ID: {}", report.getReportId());

        // 엔티티 리스트를 DTO 리스트로 변환
        List<MealDto> mealDtos = meals.stream()
                .map(meal -> new MealDto(
                        meal.getMealType(),
                        meal.getMealTime(),
                        meal.getPhotoUrl(),
                        meal.getFoods().stream()
                                .map(food -> new AnalyzedFoodDto(
                                        food.getName(), food.getKcal(), food.getProtein(),
                                        food.getCarbs(), food.getFat(), food.getCalcium(),
                                        food.getServingSize()
                                ))
                                .toList()
                ))
                .toList();

        log.info("mealDto 디버깅: {}", mealDtos);
        DailyReportAnalysisRequestDto requestDto = new DailyReportAnalysisRequestDto(
                report.getReportId(),
                user.getUserId(),
                mealDtos,
                webhookCallbackUrl
        );

        // WebClient로 FastAPI 서버에 요청
        webClient.post()
                .uri(analysisApiUrl)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Failed to request daily report analysis for report ID: {}", report.getReportId(), error))
                .subscribe(
                        result -> log.info("Successfully requested analysis for report ID: {}", report.getReportId()),
                        error -> {}
                );
    }

    @Transactional
    public void updateDailyReportWithAnalysis(DailyReportAnalysisResultDto result) {
        dailyReportService.updateReportWithAnalysis(result);
    }

}
