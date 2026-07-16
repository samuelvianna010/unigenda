package com.samuelvianna010.unigenda.components

import android.content.res.Configuration
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import com.samuelvianna010.unigenda.database.DummySubject
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.database.Assessment
import com.samuelvianna010.unigenda.database.AssessmentsViewModel
import com.samuelvianna010.unigenda.database.TemplateSubject
import com.samuelvianna010.unigenda.database.TemplateAssessment
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme
import com.samuelvianna010.unigenda.ui.utils.formatDate

@Composable
fun AssessmentCard(
    assessmentId: Long,
    onClick: (assessmentId: Long) -> Unit = {},
    assessmentsViewModel: AssessmentsViewModel = hiltViewModel(),
    subjectViewModel: SubjectViewModel = hiltViewModel()
) {
    val assessment = remember { assessmentsViewModel.getAssessmentById(assessmentId) }
        .collectAsState(initial = null).value ?: return
    val subject = remember {
        subjectViewModel.getSubjectById(assessment.subjectId)
    }.collectAsState(initial = null).value ?: DummySubject

    AssessmentCardContent(
        assessment = assessment,
        subject = subject,
        onClick = onClick
    )
}

@Composable
fun AssessmentCardContent(
    assessment: Assessment,
    subject: Subject,
    onClick: (assessmentId: Long) -> Unit = {}
) {
    val subjectColorScheme = subject.color.getColorScheme(isSystemInDarkTheme())
    val interactionSource = remember { MutableInteractionSource() }
    val urgencyColor = assessment.urgencyLevel.color

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = { onClick(assessment.id) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Box(Modifier.background(subjectColorScheme.primary, CircleShape).height(24.dp).width(24.dp))
                        Text(
                            subject.name,
                            color = subjectColorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 3.em
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Box(
                            Modifier
                                .background(urgencyColor, CircleShape)
                                .size(24.dp)
                        )
                        Text(
                            "${assessment.type.label} - ${assessment.date.formatDate("dd/MM")}",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 2.8.em
                        )
                    }
                }
                Text(
                    text = assessment.name,
                    color = subjectColorScheme.onSurface,
                    fontSize = 6.em,
                    lineHeight = 0.9.em,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(
    device = "spec:width=411dp,height=891dp",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AssessmentCardPreview() {
    UnigendaTheme {
        AssessmentCardContent(
            assessment = TemplateAssessment,
            subject = TemplateSubject,
            onClick = {}
        )
    }
}
