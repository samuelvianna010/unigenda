package com.samuelvianna010.unigenda.screens.addSubject

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.core.ui.SubjectColor
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectScreen(
	// O ViewModel agora é um parâmetro normal.
	// No app real, o Hilt preenche. No Preview, fica null.
	viewModel: SubjectViewModel? = null,
	onBack: () -> Unit = {}
) {
	var subjectName by remember { mutableStateOf("") }
	var professorName by remember {
		mutableStateOf<String>(
			""
		)
	}
	var selectedColor by remember {
		mutableStateOf(
			SubjectColor.PURPLE
		)
	}
	var isDropdownExpanded by remember {
		mutableStateOf(
			false
		)
	}
	val isDark = isSystemInDarkTheme()
	val subjectColorScheme =
		remember(selectedColor, isDark) {
			selectedColor.getColorScheme(isDark)
		}
	// ESTADOS DE ERRO
	var subjectNameError by remember { mutableStateOf<String?>(null) }
	var professorNameError by remember { mutableStateOf<String?>(null) }

	SetStatusBarColor(subjectColorScheme.primary)

	fun onSaveClick() {
		// Reseta erros anteriores
		subjectNameError = null
		professorNameError = null
		var hasError = false
		// Validação do Nome
		if (subjectName.isBlank()) {
			subjectNameError = "O nome da matéria é obrigatório"
			hasError = true
		}


		if (professorName.isBlank()) {
			professorNameError = "O nome do professor é obrigatório"
			hasError = true
		}


		if (!hasError) {
			println("Salvando disciplina")
			viewModel?.addSubject(
				subjectName,
				professorName,
				selectedColor.color.toArgb()
			)
			onBack()
		}
	}

	Scaffold(
		containerColor = subjectColorScheme.background,
	) { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding),
			verticalArrangement = Arrangement.spacedBy(
				16.dp
			)
		) {
			// ... (Mantenha todo o seu código de UI exatamente igual aqui) ...
			// Header
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
					if (subjectName.isBlank()) "Nome da Matéria" else subjectName,
					color = subjectColorScheme.onPrimary,
					fontSize = 10.em,
					maxLines = 3,
					fontWeight = FontWeight.ExtraBold,
					lineHeight = 0.9.em
				)
			}

			Column(
				modifier = Modifier
					.padding(
						horizontal = 24.dp
					)
					.padding(bottom = 16.dp),
				verticalArrangement = Arrangement.spacedBy(
					16.dp
				)
			) {
				OutlinedTextField(
					value = subjectName,
					onValueChange = {
						subjectName = it
					},
					label = { Text("Nome da Matéria") },
					placeholder = { Text("Ex: Cálculo I") },
					isError = subjectNameError != null, // Ativa o estilo de erro
					supportingText = {
						if (subjectNameError != null) {
							Text(subjectNameError!!)
						}
					},
					modifier = Modifier.fillMaxWidth(),
					singleLine = true,
					colors = OutlinedTextFieldDefaults.colors(
						focusedBorderColor = subjectColorScheme.primary,
						focusedLabelColor = subjectColorScheme.primary,
						unfocusedBorderColor = subjectColorScheme.secondary,
						unfocusedLabelColor = subjectColorScheme.secondary,
						focusedTextColor = subjectColorScheme.onSurface,
						unfocusedTextColor = subjectColorScheme.onSurface,
						cursorColor = subjectColorScheme.onSurface,
						focusedPlaceholderColor = subjectColorScheme.onSurface,
						unfocusedPlaceholderColor = subjectColorScheme.onSurface
					)
				)
				OutlinedTextField(
					value = professorName,
					onValueChange = {
						professorName = it
					},
					label = { Text("Nome do Professor") },
					placeholder = { Text("Ex: Severino Collier") },
					isError = professorNameError != null,
					supportingText = {
						if (professorNameError != null) {
							Text(professorNameError!!)
						}
					},
					modifier = Modifier.fillMaxWidth(),
					singleLine = true,
					colors = OutlinedTextFieldDefaults.colors(
						focusedBorderColor = subjectColorScheme.primary,
						focusedLabelColor = subjectColorScheme.primary,
						unfocusedBorderColor = subjectColorScheme.secondary,
						unfocusedLabelColor = subjectColorScheme.secondary,
						focusedTextColor = subjectColorScheme.onSurface,
						unfocusedTextColor = subjectColorScheme.onSurface,
						cursorColor = subjectColorScheme.onSurface,
						focusedPlaceholderColor = subjectColorScheme.onSurface,
						unfocusedPlaceholderColor = subjectColorScheme.onSurface
					)
				)


				Column {
					Text(
						text = "Cor da Matéria",
						style = MaterialTheme.typography.labelLarge,
						color = subjectColorScheme.onSurfaceVariant
					)

					Box(modifier = Modifier.fillMaxWidth()) {
						OutlinedButton(
							onClick = {
								isDropdownExpanded =
									true
							},
							modifier = Modifier.fillMaxWidth(),
							colors = ButtonDefaults.outlinedButtonColors(
								contentColor = subjectColorScheme.onSurface
							),
							border = BorderStroke(
								1.dp,
								subjectColorScheme.onSurfaceVariant
							)
						) {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween,
								modifier = Modifier.fillMaxWidth()
							) {
								Row(verticalAlignment = Alignment.CenterVertically) {
									Box(
										modifier = Modifier
											.size(
												24.dp
											)
											.clip(
												CircleShape
											)
											.background(
												selectedColor.color
											)
											.border(
												1.dp,
												subjectColorScheme.secondary,
												CircleShape
											)
									)
									Spacer(
										modifier = Modifier.width(
											12.dp
										)
									)
									Text(
										selectedColor.label
									)
								}
								Icon(
									Icons.Default.ArrowDropDown,
									contentDescription = "Abrir cores"
								)
							}
						}

						DropdownMenu(
							expanded = isDropdownExpanded,
							onDismissRequest = {
								isDropdownExpanded =
									false
							},
							modifier = Modifier
								.fillMaxWidth(
									0.9f
								)
								.background(
									subjectColorScheme.surfaceContainerLow
								)
						) {
							SubjectColor.entries.forEach { subjectColor ->
								DropdownMenuItem(
									text = {
										Row(verticalAlignment = Alignment.CenterVertically) {
											Box(
												modifier = Modifier
													.size(
														16.dp
													)
													.clip(
														CircleShape
													)
													.background(
														subjectColor.color
													)
											)
											Spacer(
												modifier = Modifier.width(
													8.dp
												)
											)
											Text(
												subjectColor.label
											)
										}
									},
									onClick = {
										selectedColor =
											subjectColor
										isDropdownExpanded =
											false
									},
									leadingIcon = {
										if (selectedColor == subjectColor) {
											Icon(
												Icons.Default.Check,
												contentDescription = "Selecionado",
												tint = subjectColorScheme.primary
											)
										}
									}
								)
							}
						}
					}
				}

				Spacer(
					modifier = Modifier.weight(
						1f
					)
				)

				Button(
					onClick = {
						if (subjectName.isNotBlank()) {
							onSaveClick()
						}
					},
					modifier = Modifier.fillMaxWidth(),
					enabled = subjectName.isNotBlank(),
					colors = ButtonDefaults.buttonColors(
						containerColor = subjectColorScheme.primary,
						contentColor = subjectColorScheme.onPrimary
					)
				) {
					Text("Salvar Matéria")
				}
			}
		}
	}
}

@Preview(
	device = "spec:width=411dp,height=891dp",
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_NO,
	showSystemUi = true
)
@Composable
fun AddSubjectScreenPreview() {
	UnigendaTheme {
		// Aqui chamamos SEM passar o ViewModel, então ele será null e seguro
		AddSubjectScreen(viewModel = null)
	}
}