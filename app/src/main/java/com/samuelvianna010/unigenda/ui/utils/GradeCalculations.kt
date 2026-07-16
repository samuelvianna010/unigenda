package com.samuelvianna010.unigenda.ui.utils

import com.samuelvianna010.unigenda.database.Assessment

/**
 * Calculates the normalized score for an assessment based on its weight percentage in the subject.
 * 
 * Formula: (assessmentScore / maxScore) * (weightPercentage / 100) * 10
 *
 * Example:
 * - Assessment: "Prova 2 de Cripto"
 * - Weight percentage: 50% (worth 50% of the subject grade)
 * - Max score: 10 (scored out of 10)
 * - Score: 6 (student got 6 out of 10)
 * - Normalized: (6 / 10) * (50 / 100) * 10 = 0.6 * 0.5 * 10 = 3.0 points
 */
fun calculateNormalizedScore(
    assessmentScore: Double,
    maxScore: Double,
    weightPercentage: Double
): Double {
    if (maxScore <= 0 || weightPercentage < 0) return 0.0
    return (assessmentScore / maxScore) * (weightPercentage / 100.0) * 10.0
}

/**
 * Calculates the total normalized score for a subject given its assessments.
 */
fun calculateTotalNormalizedScore(
    assessments: List<Assessment>
): Double {
    return assessments.sumOf { assessment ->
        val score = assessment.score ?: return@sumOf 0.0
        calculateNormalizedScore(score, assessment.maxScore, assessment.weightPercentage)
    }
}

/**
 * Calculates the percentage grade based on normalized score.
 * Returns 0-100 percentage.
 */
fun calculatePercentage(
    assessments: List<Assessment>
): Int {
    val normalizedScore = calculateTotalNormalizedScore(assessments)
    return (normalizedScore / 10.0 * 100).toInt().coerceIn(0, 100)
}
