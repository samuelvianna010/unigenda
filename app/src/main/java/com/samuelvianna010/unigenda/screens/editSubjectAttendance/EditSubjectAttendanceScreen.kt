package com.samuelvianna010.unigenda.screens.editSubjectAttendance

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.samuelvianna010.unigenda.database.DummyLectures
import com.samuelvianna010.unigenda.database.Lecture
import com.samuelvianna010.unigenda.database.LectureStatus
import com.samuelvianna010.unigenda.database.Subject
import com.samuelvianna010.unigenda.database.SubjectViewModel
import com.samuelvianna010.unigenda.database.TemplateSubject
import com.samuelvianna010.unigenda.screens.editSubjectAttendance.components.LectureStatusToggle
import com.samuelvianna010.unigenda.ui.utils.formatMonthYear
import com.samuelvianna010.unigenda.ui.utils.formatToFullDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun EditSubjectAttendanceScreen(
    subjectId: Long,
    viewModel: SubjectViewModel? = null,
    lecturesFlow: Flow<List<Lecture>>? = null,
    onBack: () -> Unit,
) {
    val subject: Subject = viewModel?.getSubjectById(subjectId)
        ?.collectAsState(initial = null)?.value ?: TemplateSubject

    val isDark = isSystemInDarkTheme()
    val subjectColorScheme = remember(subject, isDark) {
        subject.color.getColorScheme(isDark)
    }
    val subjectLectures = lecturesFlow ?: remember { viewModel?.getAllLecturesFromSubject(subjectId) }
    val groupedLectures = subjectLectures?.collectAsState(initial = emptyList())?.value?.groupBy {
        it.date.formatMonthYear()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {
        //region Header
        Box(
            modifier = Modifier
                .background(
                    subjectColorScheme.primary,
                    RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .fillMaxWidth()
                .height(250.dp)
                .statusBarsPadding()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(top = 2.dp, start = 8.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = subjectColorScheme.onPrimary
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    subject.name,
                    color = subjectColorScheme.onPrimary,
                    fontSize = 10.em,
                    maxLines = 3,
                    fontWeight = FontWeight.Bold,
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
        }
        //endregion

        //region Attendance Editing UI
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Frequência",
                color = subjectColorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,

            )
            groupedLectures?.forEach { (month, lectures) ->
                Text(
                    text = month,
                    color = subjectColorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top= 4.dp, bottom = 8.dp)
                )
                
                lectures.forEach { lecture ->
                    AttendanceItem(lecture, subjectColorScheme, viewModel)
                }
            }
        }
        //endregion
    }
}

@Composable
fun AttendanceItem(
    lecture: Lecture,
    subjectColorScheme: ColorScheme,
    viewModel: SubjectViewModel?
) {
    val backgroundColor = when (lecture.status) {
        LectureStatus.PRESENT -> subjectColorScheme.primary.copy(alpha = 0.05f)
        LectureStatus.ABSENT -> subjectColorScheme.error.copy(alpha = 0.05f)
        LectureStatus.CANCELLED -> Color.Transparent
    }
    
    val borderColor = when (lecture.status) {
        LectureStatus.PRESENT -> subjectColorScheme.primary.copy(alpha = 0.2f)
        LectureStatus.ABSENT -> subjectColorScheme.error.copy(alpha = 0.2f)
        LectureStatus.CANCELLED -> subjectColorScheme.outline.copy(alpha = 0.1f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lecture.date.formatToFullDate(),
                    fontSize = 3.3.em,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = lecture.status.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (lecture.status) {
                        LectureStatus.PRESENT -> subjectColorScheme.primary
                        LectureStatus.ABSENT -> subjectColorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            LectureStatusToggle(
                currentStatus = lecture.status,
                subjectColorScheme = subjectColorScheme,
                onStatusChange = { newStatus ->
                    viewModel?.updateLectureStatus(lecture.id, newStatus)
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        EditSubjectAttendanceScreen(
            subjectId = 123L,
            lecturesFlow = flowOf(DummyLectures),
            onBack = {}
        )
    }
}
