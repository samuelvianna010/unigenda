package com.samuelvianna010.unigenda.screens.assessmentDetails

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.database.Assessment
import com.samuelvianna010.unigenda.database.AssessmentsViewModel
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.database.TemplateAssessment
import com.samuelvianna010.unigenda.database.TemplateSubject
import com.samuelvianna010.unigenda.ui.utils.formatCompact

//region Screen Logic
@Composable
fun AssessmentDetailsScreen(
	assessmentId: Long,
	assessmentsViewModel: AssessmentsViewModel,
	subjectViewModel: SubjectViewModel,
	onEditClick: (assessmentId: Long) -> Unit,
	onBack: () -> Unit,
) {
	val assessment =
		assessmentsViewModel.getAssessmentById(assessmentId)
			.collectAsState(initial = null).value
	val subject = if (assessment != null) {
		subjectViewModel.getSubjectById(assessment.subjectId)
			.collectAsState(initial = null).value
	} else null

	if (assessment == null || subject == null) {
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
		AssessmentDetailsContent(
			assessment = assessment,
			subject = subject,
			onUpdateAssessment = {
				assessmentsViewModel.updateAssessment(
					it
				)
			},
			onEditClick = onEditClick,
			onBack = onBack 
		)
	}
}
//endregion

//region Screen UI
@Composable
fun AssessmentDetailsContent(
	assessment: Assessment,
	subject: Subject,
	onUpdateAssessment: (Assessment) -> Unit,
	onEditClick: (assessmentId: Long) -> Unit,
	onBack: () -> Unit
) {
	val subjectColorScheme = MaterialTheme.colorScheme
	var showScoreDialog by remember { mutableStateOf(false) }
	var scoreInput by remember {
		mutableStateOf(
			assessment.score?.toString() ?: ""
		)
	}

	SetStatusBarColor(subjectColorScheme.primary)

    //region Dialogs
	if (showScoreDialog) {
		AlertDialog(
			onDismissRequest = { showScoreDialog = false },
			title = { Text("Lançar Pontuação") },
			text = {
				Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
					Text("Qual foi sua pontuação em ${assessment.name}?")
					Text(
						"Peso na matéria: ${assessment.weightPercentage.formatCompact()}",
						fontWeight = FontWeight.Bold,
						fontSize = 3.em
					)
					OutlinedTextField(
						value = scoreInput,
						onValueChange = { scoreInput = it },
						label = { Text("Pontuação Obtida") },
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
						modifier = Modifier.fillMaxWidth(),
						colors = OutlinedTextFieldDefaults.colors(
							focusedBorderColor = subjectColorScheme.primary,
							focusedLabelColor = subjectColorScheme.primary
						)
					)
				}
			},
			confirmButton = {
				TextButton(
					onClick = {
						val newScore = scoreInput.toDoubleOrNull()
						onUpdateAssessment(assessment.copy(score = newScore))
						showScoreDialog = false
					}
				) {
					Text("Salvar", color = subjectColorScheme.primary)
				}
			},
			dismissButton = {
				TextButton(onClick = { showScoreDialog = false }) {
					Text("Cancelar")
				}
			}
		)
	}
    //endregion

	Scaffold(
		containerColor = subjectColorScheme.background,
	) { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding),
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
					text = assessment.name,
					color = subjectColorScheme.onPrimary,
					fontSize = 10.em,
					maxLines = 3,
					fontWeight = FontWeight.ExtraBold,
					lineHeight = 0.9.em
				)
				Text(
					"${subject.name} - ${subject.professor}",
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
					.padding(bottom = 16.dp).fillMaxHeight(),
				verticalArrangement = Arrangement.SpaceBetween
			) {
                //region Stats Grid
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
                    //region Left Column: Assessment Type
					Column(
						modifier = Modifier
							.weight(1f),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						Column(
							Modifier
								.background(
									color = subjectColorScheme.surfaceContainer,
									shape = RoundedCornerShape(20.dp)
								)
								.padding(16.dp).fillMaxWidth(),
							verticalArrangement = Arrangement.spacedBy(
								10.dp
							)
						) {
							Text(
								"Tipo",
								fontWeight = FontWeight.Bold,
								fontSize = 4.5.em
							)
							Box(
								Modifier
									.background(
										subjectColorScheme.primary,
										RoundedCornerShape(10.dp)
									)
									.padding(5.dp)
							) {
								Text(
									assessment.type.label,
									fontSize = MaterialTheme.typography.titleMedium.fontSize,
									color = subjectColorScheme.onPrimary,
									fontWeight = FontWeight.Bold
								)
							}
						}

						Column(Modifier
								   .background(
									   color = subjectColorScheme.surfaceContainer,
									   shape = RoundedCornerShape(20.dp)
								   )
								   .padding(16.dp),
							   verticalArrangement = Arrangement.spacedBy(
								   10.dp
							   )) {
							Text(
								"Peso da Avaliação",
								fontWeight = FontWeight.Bold,
								fontSize = 4.5.em
							)
							Box(
								contentAlignment = Alignment.Center,
								modifier = Modifier.fillMaxWidth()
							) {
								val progressValue = remember(assessment.weightPercentage) {
									(assessment.weightPercentage / 100.0).toFloat()
								}

								CircularProgressIndicator(
									progress = { progressValue },
									trackColor = subjectColorScheme.surfaceContainerHighest,
									color = subjectColorScheme.primary,
									strokeWidth = 20.dp,
									modifier = Modifier.size(140.dp)
								)
								
								Text(
									text = "${assessment.weightPercentage.formatCompact()}%",
									style = MaterialTheme.typography.headlineSmall,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}
                    //endregion

                    //region Right Column: Score Progress
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
							"Pontuação Obtida",
							fontWeight = FontWeight.Bold,
							fontSize = 4.5.em
						)
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier.fillMaxWidth()
						) {
							val progressValue = remember(
								assessment.score,
								assessment.maxScore
							) {
								if (assessment.maxScore > 0) {
									((assessment.score
										?: 0.0) / assessment.maxScore).coerceIn(
										0.0,
										1.0
									).toFloat()
								} else 0f
							}

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
									text = "${assessment.score.formatCompact()}/${assessment.maxScore.formatCompact()}",
									style = MaterialTheme.typography.headlineSmall,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
								if (assessment.score != null && assessment.maxScore > 0) {
									val percentage =
										(assessment.score / assessment.maxScore * 100).toInt()
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

                //region Actions Section
				Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
					Button(
						onClick = { showScoreDialog = true },
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(
							containerColor = subjectColorScheme.surfaceContainerHighest,
							contentColor = subjectColorScheme.onSurface
						)
					) {
						Text(if (assessment.score == null) "Inserir Pontuação" else "Editar Pontuação")
					}

					Button(
						onClick = { onEditClick(assessment.id) },
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
fun AssessmentDetailsPreview() {
	AssessmentDetailsContent(
		assessment = TemplateAssessment,
		subject = TemplateSubject,
		onUpdateAssessment = {},
		onEditClick = {},
		onBack = {}
	)
}
//endregion
