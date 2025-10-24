package io.github.tlsdla1235.seniormealplan.service.orchestration;

import com.amazonaws.services.s3.AmazonS3Client;
import io.github.tlsdla1235.seniormealplan.domain.Meal;
import io.github.tlsdla1235.seniormealplan.dto.meal.MealCreateRequest;
import io.github.tlsdla1235.seniormealplan.dto.s3dto.PresignedUrlResponse;
import io.github.tlsdla1235.seniormealplan.repository.MealRepository;
import io.github.tlsdla1235.seniormealplan.service.admin.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadMealService {
    private final S3UploadService s3UploadService;
    private final MealRepository mealRepository;

    public PresignedUrlResponse generatePresignedUrl(String originalFileName){
        return s3UploadService.generatePresignedUrl(originalFileName);
    }

    @Transactional
    public Meal saveMeal(Meal meal, String uniqueFileName){
        String url = s3UploadService.getFileUrl(uniqueFileName);
        meal.setPhotoUrl(url);
        mealRepository.save(meal);
        /**todo
         * ai에게 분석해달라고 하는 서비스 호출
         */

        return meal;
    }
}
