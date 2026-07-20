package com.samuelvianna010.unigenda.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureDao {
    @Query("SELECT * FROM lectures WHERE subjectId = :subjectId ORDER BY date DESC")
    fun getLecturesBySubject(subjectId: Long): Flow<List<Lecture>>

    @Query("SELECT * FROM lectures WHERE subjectId = :subjectId ORDER BY date DESC LIMIT 1")
    suspend fun getLastLectureForSubject(subjectId: Long): Lecture?

    @Insert
    suspend fun insertLectures(lectures: List<Lecture>)

    @Update
    suspend fun updateLecture(lecture: Lecture)

    @Query("UPDATE lectures SET status = :status WHERE id = :lectureId")
    suspend fun updateLectureStatus(lectureId: Long, status: LectureStatus)

    @Query("DELETE FROM lectures WHERE subjectId = :subjectId")
    suspend fun deleteLecturesBySubject(subjectId: Long)
}
