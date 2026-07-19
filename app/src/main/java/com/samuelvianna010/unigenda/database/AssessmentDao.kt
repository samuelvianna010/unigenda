package com.samuelvianna010.unigenda.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

//region Assessment DAO
@Dao
interface AssessmentDao {
    @Query("SELECT * FROM assessments ORDER BY date ASC")
    fun getAllAssessments(): Flow<List<Assessment>>

	@Query("SELECT * FROM assessments WHERE date >= :today ORDER BY date ASC")
	fun getAssessmentsFromDate(today: Long): Flow<List<Assessment>>

	fun getUpcomingAssignments(): Flow<List<Assessment>> {
		val startOfToday = LocalDate.now(ZoneId.systemDefault())
			.atStartOfDay(ZoneId.systemDefault())
			.toInstant()
			.toEpochMilli()

		return getAssessmentsFromDate(startOfToday)
	}

    @Transaction
    @Query("SELECT * FROM assessments ORDER BY date ASC")
    fun getAllAssessmentsWithSubject(): Flow<List<AssessmentWithSubject>>

    @Insert
    suspend fun insertAssessment(assessment: Assessment)

    @Update
    suspend fun updateAssessment(assessment: Assessment)

    @Query("DELETE FROM assessments WHERE id = :id")
    suspend fun deleteAssessment(id: Long)

    @Query("SELECT * FROM assessments WHERE subjectId = :subjectId")
    fun getAssessmentsBySubject(subjectId: Long): Flow<List<Assessment>>

    @Query("SELECT * FROM assessments WHERE id = :id")
    fun getAssessmentById(id: Long): Flow<Assessment?>
}
//endregion
