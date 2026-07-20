package com.samuelvianna010.unigenda.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class LectureStatus(
 var label: String
){
	PRESENT("Presente"),
	ABSENT("Ausente"),
	CANCELLED("Aula Cancelada")
}

@Entity(
	tableName = "lectures",
	foreignKeys = [ForeignKey(
		entity = Subject::class,
		parentColumns = ["id"],
		childColumns = ["subjectId"],
		onDelete = ForeignKey.CASCADE
	)]
)
data class Lecture(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val subjectId: Long,
	val date: Long,
	val status: LectureStatus = LectureStatus.PRESENT
)

val DummyLectures = listOf(
	Lecture(id = 1, subjectId = 0, date = System.currentTimeMillis(), status = LectureStatus.PRESENT),
	Lecture(id = 2, subjectId = 0, date = System.currentTimeMillis() - 86400000, status = LectureStatus.ABSENT),
	Lecture(id = 3, subjectId = 0, date = System.currentTimeMillis() - 172800000, status = LectureStatus.CANCELLED)
)