package com.samuelvianna010.unigenda.screens.editSubjectAttendance.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samuelvianna010.unigenda.database.LectureStatus
import com.samuelvianna010.unigenda.database.SubjectViewModel

@Composable
fun LectureStatusDropdown(
	id: Long,
    currentStatus: LectureStatus,
    subjectColorScheme: ColorScheme,
	viewModel: SubjectViewModel? = null,
	modifier: Modifier = Modifier
) {
	fun onStatusChange(status: LectureStatus) {
		viewModel?.updateLectureStatus(id, status)
	}

    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { isExpanded = true },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = subjectColorScheme.onSurface
            ),
            border = BorderStroke(
                1.dp,
                subjectColorScheme.onSurfaceVariant
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                    Text(currentStatus.label)
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Alterar status da aula"
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(subjectColorScheme.surfaceContainerLow)
        ) {
            LectureStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (status) {
                                            LectureStatus.PRESENT -> subjectColorScheme.primary
                                            LectureStatus.ABSENT -> subjectColorScheme.error
                                            LectureStatus.CANCELLED -> subjectColorScheme.outline
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(status.label)
                        }
                    },
                    onClick = {
                        onStatusChange(status)
                        isExpanded = false
                    },
                    leadingIcon = {
                        if (currentStatus == status) {
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

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        LectureStatusDropdown(
            id = 1L,
            currentStatus = LectureStatus.CANCELLED,
            subjectColorScheme = MaterialTheme.colorScheme,
            viewModel = null
        )
    }
}
