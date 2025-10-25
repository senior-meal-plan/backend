package io.github.tlsdla1235.seniormealplan.dto.meal;

import java.math.BigDecimal;

public record AnalyzedFoodDto(
        String name,           // 분석된 음식 이름
        BigDecimal kcal,         // 칼로리
        BigDecimal protein,      // 단백질
        BigDecimal carbs,        // 탄수화물
        BigDecimal fat,          // 지방
        BigDecimal calcium,      // 칼슘
        BigDecimal servingSize   // 섭취량 (예: 1.0인분, 0.5인분)
) {
}
