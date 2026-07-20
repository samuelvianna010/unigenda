package com.samuelvianna010.unigenda.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelvianna010.unigenda.database.DaysOfTheWeek

/**
 * Weekday selector composable.
 * selectedDays: Set of DaysOfTheWeek
 * onToggleDay: toggles a day in the parent state
 */
@Composable
fun WeekdaySelector(
    selectedDays: Set<DaysOfTheWeek>,
    onToggleDay: (DaysOfTheWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        DaysOfTheWeek.entries.forEach { day ->
            val selected = day in selectedDays

            OutlinedButton(
                onClick = { onToggleDay(day) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(40.dp).weight(1f)
            ) {
                Text(
                    text = day.shortLabel[0].toString(),
                    fontSize = 14.sp,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
