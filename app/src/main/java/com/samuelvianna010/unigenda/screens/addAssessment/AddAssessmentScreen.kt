package com.samuelvianna010.unigenda.screens.addAssessment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.samuelvianna010.unigenda.core.ui.SetStatusBarColor
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.AssessmentsViewModel
import com.samuelvianna010.unigenda.database.AssessmentType
import com.samuelvianna010.unigenda.ui.theme.UnigendaTheme
import com.samuelvianna010.unigenda.ui.utils.formatDate
import com.samuelvianna010.unigenda.ui.utils.toLocalMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssessmentScreen(
    viewModel: AssessmentsViewModel? = null,
    onBack: () -> Unit = {}
) {
    var assessmentName by remember { mutableStateOf("") }
    val subjects by viewModel?.allSubjects?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList<Subject>()) }
    
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }
    var selectedType by remember { mutableStateOf(AssessmentType.TEST) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var weightStr by remember { mutableStateOf("0.0") }
    var scoreStr by remember { mutableStateOf("") }
    
    var isSubjectDropdownExpanded by remember { mutableStateOf(false) }
    var isTypeDropdownExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val isDark = isSystemInDarkTheme()
    val defaultColorScheme = MaterialTheme.colorScheme
    val subjectColorScheme = remember(selectedSubject, isDark) {
        selectedSubject?.color?.getColorScheme(isDark) ?: defaultColorScheme
    }

    var nameError by remember { mutableStateOf<String?>(null) }
    var subjectError by remember { mutableStateOf<String?>(null) }
    var weightError by remember { mutableStateOf<String?>(null) }

    SetStatusBarColor(subjectColorScheme.primary)

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate = it.toLocalMillis() }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = subjectColorScheme.primary)
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = subjectColorScheme.primary)
                ) { Text("Cancelar") }
            },
            colors = DatePickerDefaults.colors(
                containerColor = subjectColorScheme.surfaceContainer
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = subjectColorScheme.primary,
                    selectedDayContainerColor = subjectColorScheme.primary,
                    selectedDayContentColor = subjectColorScheme.onPrimary,
                    todayDateBorderColor = subjectColorScheme.primary
                )
            )
        }
    }

    fun onSaveClick() {
        nameError = null
        subjectError = null
        weightError = null
        var hasError = false
        
        if (assessmentName.isBlank()) {
            nameError = "O nome da avaliação é obrigatório"
            hasError = true
        }
        if (selectedSubject == null) {
            subjectError = "Selecione uma matéria"
            hasError = true
        }
        
        val weight = weightStr.toDoubleOrNull() ?: -1.0
        if (weight < 0.0 || weight > 10.0) {
            weightError = "O valor deve ser entre 0 e 10"
            hasError = true
        }

        if (!hasError) {
            viewModel?.addAssessment(
                name = assessmentName,
                subjectId = selectedSubject!!.id,
                date = selectedDate,
                type = selectedType,
                weight = weight,
                score = scoreStr.toDoubleOrNull()
            )
            onBack()
        }
    }

    MaterialTheme(colorScheme = subjectColorScheme) {
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
                Column(
                    modifier = Modifier
                        .background(
                            subjectColorScheme.primary,
                            RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        if (assessmentName.isBlank()) "Nome da Avaliação" else assessmentName,
                        color = subjectColorScheme.onPrimary,
                        fontSize = 10.em,
                        maxLines = 3,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 0.9.em
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = assessmentName,
                        onValueChange = { assessmentName = it },
                        label = { Text("Nome da Avaliação") },
                        placeholder = { Text("Ex: Prova de Cálculo I") },
                        isError = nameError != null,
                        supportingText = { if (nameError != null) Text(nameError!!) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = subjectColorScheme.primary,
                            focusedLabelColor = subjectColorScheme.primary,
                            unfocusedBorderColor = subjectColorScheme.onSurfaceVariant,
                            unfocusedLabelColor = subjectColorScheme.onSurfaceVariant,
                            focusedTextColor = subjectColorScheme.onSurface,
                            unfocusedTextColor = subjectColorScheme.onSurface,
                            cursorColor = subjectColorScheme.primary
                        )
                    )

                    Column {
                        Text(
                            text = "Matéria",
                            style = MaterialTheme.typography.labelLarge,
                            color = subjectColorScheme.onSurfaceVariant
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { isSubjectDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, if (subjectError != null) MaterialTheme.colorScheme.error else subjectColorScheme.onSurfaceVariant),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = subjectColorScheme.onSurface)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = selectedSubject?.name ?: "Selecione a matéria",
                                        color = subjectColorScheme.onSurface
                                    )
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = subjectColorScheme.onSurface)
                                }
                            }
                            DropdownMenu(
                                expanded = isSubjectDropdownExpanded,
                                onDismissRequest = { isSubjectDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f).background(subjectColorScheme.surfaceContainer)
                            ) {
                                subjects.forEach { subject ->
                                    DropdownMenuItem(
                                        text = { Text(subject.name, color = subjectColorScheme.onSurface) },
                                        onClick = {
                                            selectedSubject = subject
                                            isSubjectDropdownExpanded = false
                                            subjectError = null
                                        },
                                        leadingIcon = {
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clip(CircleShape)
                                                    .background(subject.color.color)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        if (subjectError != null) {
                            Text(
                                text = subjectError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = weightStr,
                            onValueChange = { weightStr = it },
                            label = { Text("Valor (0-10)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = weightError != null,
                            supportingText = { if (weightError != null) Text(weightError!!) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = subjectColorScheme.primary,
                                focusedLabelColor = subjectColorScheme.primary,
                                cursorColor = subjectColorScheme.primary
                            )
                        )
                        OutlinedTextField(
                            value = scoreStr,
                            onValueChange = { scoreStr = it },
                            label = { Text("Nota Obtida") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = subjectColorScheme.primary,
                                focusedLabelColor = subjectColorScheme.primary,
                                cursorColor = subjectColorScheme.primary
                            )
                        )
                    }

                    Column {
                        Text(
                            text = "Tipo de Avaliação",
                            style = MaterialTheme.typography.labelLarge,
                            color = subjectColorScheme.onSurfaceVariant
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { isTypeDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, subjectColorScheme.onSurfaceVariant),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = subjectColorScheme.onSurface)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = selectedType.label, color = subjectColorScheme.onSurface)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = subjectColorScheme.onSurface)
                                }
                            }
                            DropdownMenu(
                                expanded = isTypeDropdownExpanded,
                                onDismissRequest = { isTypeDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f).background(subjectColorScheme.surfaceContainer)
                            ) {
                                AssessmentType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.label, color = subjectColorScheme.onSurface) },
                                        onClick = {
                                            selectedType = type
                                            isTypeDropdownExpanded = false
                                        },
                                        leadingIcon = {
                                            if (selectedType == type) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = subjectColorScheme.primary)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column {
                        Text(
                            text = "Data de Entrega",
                            style = MaterialTheme.typography.labelLarge,
                            color = subjectColorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, subjectColorScheme.onSurfaceVariant),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = subjectColorScheme.onSurface)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = selectedDate.formatDate(), color = subjectColorScheme.onSurface)
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = subjectColorScheme.onSurface)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { onSaveClick() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = assessmentName.isNotBlank() && selectedSubject != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = subjectColorScheme.primary,
                            contentColor = subjectColorScheme.onPrimary
                        )
                    ) {
                        Text("Salvar Avaliação")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddAssessmentScreenPreview() {
    UnigendaTheme {
        AddAssessmentScreen()
    }
}
