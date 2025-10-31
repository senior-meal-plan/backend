package io.github.tlsdla1235.seniormealplan.dto.meal;

import java.math.BigDecimal;
import java.util.List;

public record AnalysisMealResultDto(
        Long mealId,           // 어떤 식단에 대한 분석 결과인지 식별자
        BigDecimal totalKcal,  // 분석된 총 칼로리
        BigDecimal totalProtein,
        BigDecimal totalCarbs,
        BigDecimal totalFat,
        BigDecimal totalCalcium,
        String Summary,
        String Severity,

//        추가된 항목
        
        BigDecimal averageSaturatedFatPercentKcal, // 포화지방 (%kcal) - 식단 전체 평균
        BigDecimal totalUnsaturatedFat,          // 불포화지방 (g)
        BigDecimal totalDietaryFiber,            // 식이섬유 (g)
        BigDecimal totalSodium,                  // 나트륨 (mg)
        BigDecimal totalAddedSugarKcal,          // 첨가당 (kcal)
        BigDecimal totalProcessedMeatGram,       // 가공육 (g)
        BigDecimal totalVitaminD_IU,             // 비타민 D (UI)
        boolean isDairyIntake,
        boolean isVitaminCIntake,
        boolean isVitaminBIntake,
        boolean isFishIntake,
        boolean isNutsIntake,
        boolean isVegetableOilIntake,
        boolean isUnrefinedCarbsIntake,

        List<AnalyzedFoodDto> foods
) {
}
