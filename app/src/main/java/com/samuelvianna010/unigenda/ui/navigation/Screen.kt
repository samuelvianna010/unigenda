package com.samuelvianna010.unigenda.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object AddSubject : Screen

    @Serializable
    data object AddAssessment : Screen

    @Serializable
    data class AssessmentDetails(val assessmentId: Long) : Screen

    @Serializable
    data class SubjectDetails(val subjectId: Long) : Screen

    @Serializable
    data class EditOrDeleteSubject(val subjectId: Long) : Screen

    @Serializable
    data class EditOrDeleteAssessment(val assessmentId: Long) : Screen
}
