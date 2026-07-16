package com.samuelvianna010.unigenda.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//region Subject DAO
@Dao
interface SubjectDao {
	@Query("SELECT * FROM subjects ORDER BY name ASC")
	fun getAllSubjects(): Flow<List<Subject>>

	@Insert
	suspend fun insertSubject(subject: Subject)

	@Update
	suspend fun updateSubject(subject: Subject)

	@Query("DELETE FROM subjects WHERE id = :id")
	suspend fun deleteSubject(id: Long)

	@Query("SELECT * FROM subjects WHERE id = :id")
	fun getSubjectById(id: Long): Flow<Subject?>
}
//endregion