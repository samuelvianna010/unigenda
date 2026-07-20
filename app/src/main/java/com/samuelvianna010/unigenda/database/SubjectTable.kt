package com.samuelvianna010.unigenda.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelvianna010.unigenda.core.ui.SubjectColor

enum class DaysOfTheWeek(val shortLabel: String, val longLabel: String) {
	MONDAY("Seg", "Segunda-feira"),
	TUESDAY("Ter", "Terça-feira"),
	WEDNESDAY("Qua", "Quarta-feira"),
	THURSDAY("Qui", "Quinta-feira"),
	FRIDAY("Sex", "Sexta-feira"),
	SATURDAY("Sáb", "Sábado"),
	SUNDAY("Dom", "Domingo")
}

//region Subject Entity
@Entity(tableName = "subjects")
data class Subject(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val name: String,
	val professor: String,
	val colorInt: Int,
	val totalWeight: Double = 10.0,
	val dateStart: Long,
	val dateEnd: Long,
	val lectureDays: Set<DaysOfTheWeek>
) {
	val color: SubjectColor
		get() = SubjectColor.fromColorValue(
			colorInt
		) ?: SubjectColor.PURPLE
}
//endregion

//region Dummy Data
val DummySubject = Subject(
	0L,
	"Dummy Subject Name",
	"Dummy Professor Name",
	SubjectColor.PURPLE.color.value.toInt(),
	totalWeight = 10.0,
	dateStart = 0L,
	dateEnd = 0L,
	lectureDays = emptySet()
)
//endregion