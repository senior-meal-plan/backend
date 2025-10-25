package io.github.tlsdla1235.seniormealplan.service.orchestration;


import io.github.tlsdla1235.seniormealplan.dto.dailyreport.GenerateDailyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateDailyReportService {

    private final WebClient webClient;
    @Value("${service.analysis.url}")
    private String analysisApiUrl; // FastAPI 서버 주소 (application.yml)
    @Value("${service.webhook.callback-url}")
    private String webhookCallbackUrl; // 우리 웹훅 주소 (application.yml)



    @Transactional
    public void RequestGenerateDailyReport() {
        log.info("Request Generate daily report");
        GenerateDailyRequest generateDailyRequest = new GenerateDailyRequest("something");

    }

}
