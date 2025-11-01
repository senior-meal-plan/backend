package io.github.tlsdla1235.seniormealplan.service.orchestration;

import io.github.tlsdla1235.seniormealplan.domain.User;
import io.github.tlsdla1235.seniormealplan.domain.report.WeeklyReport;
import io.github.tlsdla1235.seniormealplan.dto.user.WhoAmIDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.AnalysisResultDto.WeeklyAnalysisResultDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.DailyReportsForWeeklyReportDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.MealForWeeklyDto;
import io.github.tlsdla1235.seniormealplan.dto.weeklyreport.WeeklyReportGenerateRequestDto;
import io.github.tlsdla1235.seniormealplan.service.food.FoodService;
import io.github.tlsdla1235.seniormealplan.service.food.MealService;
import io.github.tlsdla1235.seniormealplan.service.recipe.RecipeRecommendService;
import io.github.tlsdla1235.seniormealplan.service.report.DailyReportService;
import io.github.tlsdla1235.seniormealplan.service.report.MealReportService;
import io.github.tlsdla1235.seniormealplan.service.report.WeeklyReportService;
import io.github.tlsdla1235.seniormealplan.service.user.AiManagementGoalService;
import io.github.tlsdla1235.seniormealplan.service.user.UserService;
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
@RequiredArgsConstructor
@Slf4j
public class GenerateWeeklyReportsService {
    private final WeeklyReportService weeklyReportService;
    private final DailyReportService dailyReportService;
    private final MealReportService mealReportService;

    private final MealService mealService;
    private final FoodService foodService;
    private final UserService userService;
    private final WebClient webClient;

    private final AiManagementGoalService aiManagementGoalService;
    private final RecipeRecommendService recipeRecommendService;

    @Value("${service.weekly.analysis.url}")
    private String analysisApiUrl; // FastAPI 서버 주소 (application.yml)

    @Value("${service.webhook.callback-url}")
    private String webhookCallbackUrl; // 우리 웹훅 주소 (application.yml)

    @Transactional
    public void createRequestDto(User user)
    {
        LocalDate today = LocalDate.now();
        log.info("Create Weekly Report request dto");
        WhoAmIDto userdto = userService.whoAmI(user);
        log.info("userdto: {}", userdto);
        List<DailyReportsForWeeklyReportDto> dailyReportDto = dailyReportService.getCompletedReportsForLastWeek(user, today);
        log.info("dailyReportDto: {}", dailyReportDto);
        List<MealForWeeklyDto> mealsDto = mealService.getMealsForLastWeek(user, today);
        log.info("mealsDto: {}", mealsDto);

        WeeklyReport newReport = weeklyReportService.createPendingWeeklyReport(user,today);

        WeeklyReportGenerateRequestDto requestDto = new WeeklyReportGenerateRequestDto(userdto, dailyReportDto, mealsDto);
        requestAnalysisToExternalApi(newReport, requestDto);
    }

    @Async
    public void requestAnalysisToExternalApi(WeeklyReport report, WeeklyReportGenerateRequestDto requestDto)
    {
        log.info("Starting async request for weekly report analysis. Report ID: {}", report.getReportId());

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
    public void updateWeeklyResult(WeeklyAnalysisResultDto resultDto)
    {
        /**todo
         * 1. weeklyReport 업데이트
         * 2. 유저 건강목표 설정
         * 3. 유저 추천 음식 서정
         */
        User user = User.builder().userId(resultDto.UserId()).build();
        weeklyReportService.updatePendingWeeklyReport(resultDto);
        aiManagementGoalService.updateAiSelectedTopics(user, resultDto.aiRecommendTopic());
        recipeRecommendService.createWeeklyRecommendations(user, resultDto);
    }
}
