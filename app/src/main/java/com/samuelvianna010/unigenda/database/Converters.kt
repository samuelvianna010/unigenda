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
}
//endregion
