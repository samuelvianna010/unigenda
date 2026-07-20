package com.samuelvianna010.unigenda.ui.utils

import com.samuelvianna010.unigenda.database.Lecture
import com.samuelvianna010.unigenda.database.LectureStatus

/**
 * Calculates the presence percentage.
 * Formula: (Present Lectures / (Present + Absent Lectures)) * 100
 */
fun calculatePresencePercentage(lectures: List<Lecture>): Int {
    val relevantLectures = lectures.filter { it.status == LectureStatus.PRESENT || it.status == LectureStatus.ABSENT }
    if (relevantLectures.isEmpty()) return 100
    val presentCount = relevantLectures.count { it.status == LectureStatus.PRESENT }
    return (presentCount.toDouble() / relevantLectures.size * 100).toInt()
}

/**
 * Counts total absences.
 */
fun countAbsences(lectures: List<Lecture>): Int {
    return lectures.count { it.status == LectureStatus.ABSENT }
}
