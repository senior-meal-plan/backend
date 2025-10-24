package io.github.tlsdla1235.seniormealplan.dto.meal;

import java.math.BigDecimal;

public record AnalysisMealResultDto(
        Long mealId,           // 어떤 식단에 대한 분석 결과인지 식별자
        BigDecimal totalKcal,  // 분석된 총 칼로리
        BigDecimal totalProtein,
        BigDecimal totalCarbs,
        BigDecimal totalFat
) {
}
