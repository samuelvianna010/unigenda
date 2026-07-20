package com.samuelvianna010.unigenda.screens.editSubjectAttendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.samuelvianna010.unigenda.database.LectureStatus

@Composable
fun LectureStatusToggle(
    currentStatus: LectureStatus,
    subjectColorScheme: ColorScheme,
    onStatusChange: (LectureStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusToggleItem(
            status = LectureStatus.PRESENT,
            isSelected = currentStatus == LectureStatus.PRESENT,
            icon = Icons.Default.Check,
            selectedColor = subjectColorScheme.primary,
            onSelect = { onStatusChange(LectureStatus.PRESENT) }
        )
        StatusToggleItem(
            status = LectureStatus.ABSENT,
            isSelected = currentStatus == LectureStatus.ABSENT,
            icon = Icons.Default.Close,
            selectedColor = subjectColorScheme.error,
            onSelect = { onStatusChange(LectureStatus.ABSENT) }
        )
        StatusToggleItem(
            status = LectureStatus.CANCELLED,
            isSelected = currentStatus == LectureStatus.CANCELLED,
            icon = Icons.Default.Refresh,
            selectedColor = subjectColorScheme.outline,
            onSelect = { onStatusChange(LectureStatus.CANCELLED) }
        )
    }
}

@Composable
private fun StatusToggleItem(
    status: LectureStatus,
    isSelected: Boolean,
    icon: ImageVector,
    selectedColor: Color,
    onSelect: () -> Unit
) {
    IconButton(
        onClick = onSelect,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) selectedColor.copy(alpha = 0.2f) else Color.Transparent)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status.label,
            tint = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )
    }
}
