package com.samuelvianna010.unigenda.screens.editOrDeleteSubject

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.graphics.ColorUtils
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.core.ui.SubjectColor
import com.samuelvianna010.unigenda.database.DummySubject
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOrDeleteSubjectScreen(
	subjectId: Long,
	viewModel: SubjectViewModel? = null,
	onBack: () -> Unit = {},
	onDelete: () -> Unit = onBack
) {
	// LOG PARA DEBUG
	println("🔍 EditOrDeleteSubjectScreen: subjectId = $subjectId, viewModel = $viewModel")
	val subjectState = if (viewModel != null) {
		viewModel.getSubjectById(subjectId)
			.collectAsState(initial = null)
	} else {
		remember { mutableStateOf(DummySubject) }
	}
	
	// Mantemos uma referência à última versão não-nula da matéria para evitar
	// que a tela mostre um loading screen durante a animação de saída após exclusão.
	var lastNonNullSubject by remember { mutableStateOf<Subject?>(null) }
	if (subjectState.value != null) {
		lastNonNullSubject = subjectState.value
	}
	
	val subject = lastNonNullSubject

	if (subject == null) {
		println("⚠️ Subject é null! Mostrando loading...")
		Box(
			Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator()
		}
		return
	}
	var subjectName by remember {
		mutableStateOf(
			subject.name
		)
	}
	var professorName by remember {
		mutableStateOf<String>(
			subject.professor
		)
	}
	var totalWeightStr by remember {
		mutableStateOf<String>(
			subject.totalWeight.toString()
		)
	}
	var selectedColor by remember {
		mutableStateOf(
			subject.color
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
	var subjectNameError by remember {
		mutableStateOf<String?>(
			null
		)
	}
	var professorNameError by remember {
		mutableStateOf<String?>(
			null
		)
	}
	var totalWeightError by remember {
		mutableStateOf<String?>(
			null
		)
	}

	var showDeleteDialog by remember { mutableStateOf(false) }

	SetStatusBarColor(subjectColorScheme.primary)

	fun onSaveClick() {
		// Reseta erros anteriores
		subjectNameError = null
		professorNameError = null
		totalWeightError = null
		var hasError = false
		// Validação do Nome
		if (subjectName.isBlank()) {
			subjectNameError =
				"O nome da matéria é obrigatório"
			hasError = true
		}


		if (professorName.isBlank()) {
			professorNameError =
				"O nome do professor é obrigatório"
			hasError = true
		}

		val totalWeight = totalWeightStr.toDoubleOrNull()
		if (totalWeight == null || totalWeight <= 0) {
			totalWeightError = "Peso total deve ser um número maior que 0"
			hasError = true
		}

		if (!hasError) {
			println("Salvando disciplina")
			viewModel?.updateSubject(
				Subject(
					subject.id,
					subjectName,
					professorName,
					selectedColor.color.toArgb(),
					totalWeight ?: 10.0
				)
			)
			onBack()
		}
	}

	Scaffold(
		containerColor = subjectColorScheme.background,
	) { padding ->
		if (showDeleteDialog) {
			AlertDialog(
				onDismissRequest = { showDeleteDialog = false },
				title = { Text("Confirmar Exclusão") },
				text = { Text("Deseja realmente excluir \"${subject.name}\"?") },
				confirmButton = {
					TextButton(
						onClick = {
							viewModel?.deleteSubject(subject.id)
							showDeleteDialog = false
							onDelete() // Volta para onde for definido (ex: Home)
						},
						colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
					) { Text("Sim, Excluir") }
				},
				dismissButton = {
					TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
				}
			)
		}
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
				,
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
							Text(
								professorNameError!!
							)
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
					value = totalWeightStr,
					onValueChange = {
						totalWeightStr = it
					},
					label = { Text("Peso Total da Matéria") },
					placeholder = { Text("Ex: 10.0") },
					isError = totalWeightError != null,
					supportingText = {
						if (totalWeightError != null) {
							Text(
								totalWeightError!!
							)
						}
					},
					modifier = Modifier.fillMaxWidth(),
					singleLine = true,
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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

				Column {
					Button(
						onClick = { showDeleteDialog = true },
						modifier = Modifier.fillMaxWidth(),
						colors = ButtonDefaults.buttonColors(
							containerColor = subjectColorScheme.error,
							contentColor = subjectColorScheme.onError
						)
					) {
						Text("Excluir Matéria")
					}

					Button(
						onClick = {
							onSaveClick()
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
}

@Preview(
	device = "spec:width=411dp,height=891dp",
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	showSystemUi = true
)
@Composable
fun Preview() {
	UnigendaTheme {
		// Aqui chamamos SEM passar o ViewModel, então ele será null e seguro
		EditOrDeleteSubjectScreen(
			subjectId = 0
		)
	}
}