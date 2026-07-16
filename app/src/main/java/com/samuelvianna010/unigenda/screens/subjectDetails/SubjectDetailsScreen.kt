package com.samuelvianna010.unigenda.screens.subjectDetails

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.samuelvianna010.unigenda.components.AssessmentCardContent
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.database.Assessment
import com.samuelvianna010.unigenda.database.AssessmentsViewModel
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.database.TemplateSubject
import com.samuelvianna010.unigenda.ui.utils.formatCompact

//region Screen Logic
@Composable
fun SubjectDetailsScreen(
	subjectId: Long,
	subjectViewModel: SubjectViewModel,
	assessmentsViewModel: AssessmentsViewModel,
	onEditClick: (subjectId: Long) -> Unit,
	onAssessmentClick: (assessmentId: Long) -> Unit,
	onBack: () -> Unit
) {
	val subject = subjectViewModel.getSubjectById(subjectId)
		.collectAsState(initial = null).value
	val assessments = assessmentsViewModel.getAssessmentsBySubject(subjectId)
		.collectAsState(initial = emptyList()).value

	if (subject == null) {
		Box(
			Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator()
		}
		return
	}

	val isDark = isSystemInDarkTheme()
	val subjectColorScheme = remember(subject, isDark) {
		subject.color.getColorScheme(isDark)
	}

	MaterialTheme(colorScheme = subjectColorScheme) {
		SubjectDetailsContent(
			subject = subject,
			assessments = assessments,
			onEditClick = onEditClick,
			onAssessmentClick = onAssessmentClick,
			onBack = onBack
		)
	}
}
//endregion

//region Screen UI
@Composable
fun SubjectDetailsContent(
	subject: Subject,
	assessments: List<Assessment>,
	onEditClick: (subjectId: Long) -> Unit,
	onAssessmentClick: (assessmentId: Long) -> Unit,
	onBack: () -> Unit
) {
	val subjectColorScheme = MaterialTheme.colorScheme
	SetStatusBarColor(subjectColorScheme.primary)

	val totalWeight = assessments.sumOf { it.weight }
	val currentScore = assessments.sumOf { it.score ?: 0.0 }
	val totalGradedWeight = assessments.filter { it.score != null }.sumOf { it.weight }

	Scaffold(
		containerColor = subjectColorScheme.background,
	) { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			//region Header Section
			Column(
				modifier = Modifier
					.background(
						subjectColorScheme.primary,
						RoundedCornerShape(
							bottomStart = 24.dp,
							bottomEnd = 24.dp
						)
					)
					.padding(horizontal = 16.dp)
					.fillMaxWidth()
					.height(250.dp),
				verticalArrangement = Arrangement.Center
			) {
				Text(
					text = subject.name,
					color = subjectColorScheme.onPrimary,
					fontSize = 10.em,
					maxLines = 3,
					fontWeight = FontWeight.ExtraBold,
					lineHeight = 0.9.em
				)
				Text(
					text = subject.professor,
					color = subjectColorScheme.onPrimary,
					fontSize = 4.em,
					maxLines = 3,
					fontWeight = FontWeight.SemiBold,
					lineHeight = 0.9.em
				)
			}
            //endregion

			Column(
				modifier = Modifier
					.padding(horizontal = 24.dp)
					.padding(bottom = 16.dp),
				verticalArrangement = Arrangement.spacedBy(24.dp)
			) {
                //region Stats Grid
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
					//region Left Card: Assessment Count
					Column(
						modifier = Modifier
							.weight(1f)
							.background(
								color = subjectColorScheme.surfaceContainer,
								shape = RoundedCornerShape(20.dp)
							)
							.padding(16.dp),
						verticalArrangement = Arrangement.spacedBy(10.dp)
					) {
						Text(
							"Avaliações",
							fontWeight = FontWeight.Bold,
							fontSize = 4.5.em
						)
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier.fillMaxWidth()
						) {
							val progressValue = (totalWeight / 10f).coerceIn(0.0, 1.0).toFloat()
							
							CircularProgressIndicator(
								progress = { progressValue },
								trackColor = subjectColorScheme.surfaceContainerHighest,
								color = subjectColorScheme.primary,
								strokeWidth = 20.dp,
								modifier = Modifier.size(140.dp)
							)
							
							Column(
								horizontalAlignment = Alignment.CenterHorizontally,
								verticalArrangement = Arrangement.Center
							) {
								Text(
									text = assessments.size.toString(),
									style = MaterialTheme.typography.headlineSmall,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
								Text(
									text = "Total: ${totalWeight.formatCompact()}",
									style = MaterialTheme.typography.bodySmall,
									textAlign = TextAlign.Center
								)
							}
						}
					}
                    //endregion

					//region Right Card: Current Grade
					Column(
						modifier = Modifier
							.weight(1f)
							.background(
								color = subjectColorScheme.surfaceContainer,
								shape = RoundedCornerShape(20.dp)
							)
							.padding(16.dp),
						verticalArrangement = Arrangement.spacedBy(10.dp)
					) {
						Text(
							"Nota Acumulada",
							fontWeight = FontWeight.Bold,
							fontSize = 4.5.em
						)
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier.fillMaxWidth()
						) {
							val progressValue = if (totalWeight > 0) (currentScore / 10f).coerceIn(0.0, 1.0).toFloat() else 0f

							CircularProgressIndicator(
								progress = { progressValue },
								trackColor = subjectColorScheme.surfaceContainerHighest,
								color = subjectColorScheme.primary,
								strokeWidth = 20.dp,
								modifier = Modifier.size(140.dp)
							)
							Column(
								horizontalAlignment = Alignment.CenterHorizontally,
								verticalArrangement = Arrangement.Center
							) {
								Text(
									text = currentScore.formatCompact(),
									style = MaterialTheme.typography.headlineSmall,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
								if (totalGradedWeight > 0) {
									val percentage = (currentScore / totalGradedWeight * 100).toInt()
									Text(
										text = "($percentage%)",
										style = MaterialTheme.typography.bodyMedium,
										textAlign = TextAlign.Center
									)
								}
							}
						}
					}
                    //endregion
				}
                //endregion

				//region Assessment List Section
				Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
					Text(
						"Lista de Avaliações",
						fontWeight = FontWeight.Bold,
						fontSize = 5.em,
						color = subjectColorScheme.onBackground
					)
					
					if (assessments.isEmpty()) {
						Text(
							"Nenhuma avaliação cadastrada.",
							style = MaterialTheme.typography.bodyMedium,
							color = subjectColorScheme.onSurfaceVariant
						)
					} else {
						assessments.forEach { assessment ->
							AssessmentCardContent(
								assessment = assessment,
								subject = subject,
								onClick = onAssessmentClick
							)
						}
					}
				}
                //endregion

                //region Actions Section
				Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
					Button(
						onClick = { onEditClick(subject.id) },
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(
							containerColor = subjectColorScheme.primary,
							contentColor = subjectColorScheme.onPrimary
						)
					) {
						Text("Mais Opções")
					}
				}
                //endregion
			}
		}
	}
}
//endregion

//region Preview
@Preview(
	device = "spec:width=411dp,height=891dp",
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SubjectDetailsPreview() {
	SubjectDetailsContent(
		subject = TemplateSubject,
		assessments = emptyList(),
		onEditClick = {},
		onAssessmentClick = {},
		onBack = {}
	)
}
//endregion
