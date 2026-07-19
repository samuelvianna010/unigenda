package com.samuelvianna010.unigenda.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samuelvianna010.unigenda.components.ExpandableFAB
import com.samuelvianna010.unigenda.components.SubjectCard
import com.samuelvianna010.unigenda.components.AssessmentCard
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.database.Assessment
import com.samuelvianna010.unigenda.database.AssessmentsViewModel
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme

//region Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onViewSubjectDetails: (subjectId: Long) -> Unit = {},
    onViewAssessmentDetails: (assessmentId: Long) -> Unit = {},
    onAddSubject: () -> Unit = {},
    onAddAssessment: () -> Unit = {},
    subjectViewModel: SubjectViewModel? = null,
    assessmentsViewModel: AssessmentsViewModel? = null
) {
    SetStatusBarColor(MaterialTheme.colorScheme.surfaceContainerLowest)
    //region Assessments Variables
    val allAssessmentsState = assessmentsViewModel?.getUpcomingAssessments?.collectAsState(initial = emptyList())
    val assessmentsList: List<Assessment> = allAssessmentsState?.value ?: emptyList()
    //endregion
    //region Subjects Variables
    val allSubjectsState = subjectViewModel?.allSubjects?.collectAsState(initial = emptyList())
    val subjectsList: List<Subject> = allSubjectsState?.value ?: emptyList()
    //endregion

    var isFABExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("🎓 Unigenda") }) },
        floatingActionButton = {
            ExpandableFAB(
                isExpanded = isFABExpanded,
                onToggle = { isFABExpanded = !isFABExpanded },
                onAddSubject = onAddSubject,
                onAddAssessment = onAddAssessment
            )
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //region Upcoming Assessments Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Próximas Avaliações",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
                if (assessmentsList.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        assessmentsList.forEach { assessment ->
                            AssessmentCard(
                                assessmentId = assessment.id,
                                onClick = onViewAssessmentDetails
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Não há nenhuma avaliação programada para os próximos dias.",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            //endregion

            //region Subjects Section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Disciplinas",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.SemiBold
                )
                if (subjectsList.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        subjectsList.forEach { subject ->
                            SubjectCard(subject, onClick = onViewSubjectDetails)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Ainda não há nenhuma disciplina adicionada.",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            //endregion
        }
    }
}
//endregion

//region Preview
@Preview(device = "spec:width=411dp,height=891dp", showBackground = true)
@Composable
private fun HomeScreenPreview() {
    UnigendaTheme {
        HomeScreen()
    }
}
//endregion
