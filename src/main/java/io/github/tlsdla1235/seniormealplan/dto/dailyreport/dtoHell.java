package io.github.tlsdla1235.seniormealplan.dto.dailyreport;

import io.github.tlsdla1235.seniormealplan.dto.meal.MealImageDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record dtoHell(
        Long reportId,
        LocalDate date,
        BigDecimal score,
        List<MealImageDto> meals // 그날의 식사 이미지 목록
) {
}
