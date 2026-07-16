package com.samuelvianna010.unigenda.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelvianna010.unigenda.core.ui.SubjectColor

@Entity(tableName = "subjects")
data class Subject(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val name: String,
	val professor: String,
	val colorInt: Int,
	val totalWeight: Double = 10.0
) {
	val color: SubjectColor
		get() = SubjectColor.fromColorValue(
			colorInt
		) ?: SubjectColor.PURPLE
}

val DummySubject = Subject(
	0L,
	"Dummy Subject Name",
	"Dummy Professor Name",
	SubjectColor.PURPLE.color.value.toInt(),
	totalWeight = 10.0
)