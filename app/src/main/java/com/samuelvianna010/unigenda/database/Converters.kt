package com.samuelvianna010.unigenda.database

import androidx.room.TypeConverter

//region Type Converters
class Converters {
    @TypeConverter
    fun fromUrgencyLevel(value: UrgencyLevel): String = value.name

    @TypeConverter
    fun toUrgencyLevel(value: String): UrgencyLevel = UrgencyLevel.valueOf(value)

    @TypeConverter
    fun fromAssessmentType(value: AssessmentType): String = value.name

    @TypeConverter
    fun toAssessmentType(value: String): AssessmentType = AssessmentType.valueOf(value)

    // Converters for storing lecture days (Set<DaysOfTheWeek>) as CSV in Room
    @TypeConverter
    fun fromDaysSet(value: Set<DaysOfTheWeek>): String = value.joinToString(",") { it.name }

    @TypeConverter
    fun toDaysSet(value: String): Set<DaysOfTheWeek> =
        if (value.isBlank()) emptySet() else value.split(",").map { DaysOfTheWeek.valueOf(it) }.toSet()
}
//endregion
