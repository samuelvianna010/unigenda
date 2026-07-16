package com.samuelvianna010.unigenda.database

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.Embedded
import androidx.compose.ui.graphics.toArgb
import com.samuelvianna010.unigenda.core.ui.SubjectColor

enum class UrgencyLevel(val color: Color, val label: String) {
    LOW(Color(0xFF4CAF50), "Baixa"),
    MEDIUM(Color(0xFFFFC107), "Média"),
    HIGH(Color(0xFFFF9800), "Alta"),
    URGENT(Color(0xFFF44336), "Crítica")
}

enum class AssessmentType(val label: String, val defaultUrgency: UrgencyLevel) {
    FINAL_EXAM("Exame Final", UrgencyLevel.URGENT),
    TEST("Prova/Teste", UrgencyLevel.HIGH),
    PRESENTATION("Apresentação", UrgencyLevel.MEDIUM),
    ESSAY("Redação/Resenha", UrgencyLevel.MEDIUM),
    PROJECT("Projeto", UrgencyLevel.HIGH),
    HOMEWORK("Trabalho de Casa", UrgencyLevel.LOW),
    OTHER("Outro", UrgencyLevel.LOW)
}

@Entity(
    tableName = "assessments",
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Assessment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val subjectId: Long,
    val date: Long,
    val type: AssessmentType,
    val urgencyLevel: UrgencyLevel,
    val weight: Double = 0.0, // Worth (0.0 to 10.0)
    val score: Double? = null // Actual score attained (0.0 to weight)
)

data class AssessmentWithSubject(
    @Embedded val assessment: Assessment,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "id"
    )
    val subject: Subject
)

val TemplateSubject = Subject(
    id = 0L,
    name = "Introdução ao Pensamento Dedutivo",
    professor = "Severino Collier",
    colorInt = SubjectColor.BLUE.color.toArgb()
)

val TemplateAssessment = Assessment(
    id = 0L,
    name = "Resenha do Capítulo 1",
    subjectId = 0L,
    date = System.currentTimeMillis(),
    type = AssessmentType.TEST,
    urgencyLevel = UrgencyLevel.MEDIUM,
    weight = 2.0,
    score = 1.9
)

val TemplateAssessmentWithSubject = AssessmentWithSubject(
    assessment = TemplateAssessment,
    subject = TemplateSubject
)
