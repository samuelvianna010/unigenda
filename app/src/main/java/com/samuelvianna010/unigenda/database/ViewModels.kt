package com.samuelvianna010.unigenda.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

//region Subject ViewModel
@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectDao: SubjectDao,
    private val lectureDao: LectureDao
) : ViewModel() {
    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects()

    init {
        syncLecturesForAllSubjects()
    }

    private fun syncLecturesForAllSubjects() {
        viewModelScope.launch {
            subjectDao.getAllSubjectsOnce().forEach { subject ->
                syncLecturesForSubject(subject)
            }
        }
    }

	fun getAllLecturesFromSubject(subjectId: Long): Flow<List<Lecture>> {
		return lectureDao.getLecturesBySubject(subjectId)
	}

    private suspend fun syncLecturesForSubject(subject: Subject) {
        val lastLecture = lectureDao.getLastLectureForSubject(subject.id)
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis

        // Start from the day after the last lecture, or the subject start date
        val startDate = if (lastLecture != null) {
            calendar.timeInMillis = lastLecture.date
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.timeInMillis
        } else {
            subject.dateStart
        }

        // End at today or subject end date (whichever is earlier)
        val endLimit = if (today < subject.dateEnd) today else subject.dateEnd
        
        if (startDate > endLimit) return

        val newLectures = mutableListOf<Lecture>()
        calendar.timeInMillis = startDate

        while (calendar.timeInMillis <= endLimit) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val mappedDay = mapCalendarDayToDaysOfTheWeek(dayOfWeek)

            if (mappedDay != null && mappedDay in subject.lectureDays) {
                newLectures.add(
                    Lecture(
                        subjectId = subject.id,
                        date = calendar.timeInMillis
                    )
                )
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (newLectures.isNotEmpty()) {
            lectureDao.insertLectures(newLectures)
        }
    }

    private fun mapCalendarDayToDaysOfTheWeek(dayOfWeek: Int): DaysOfTheWeek? {
        return when (dayOfWeek) {
            Calendar.MONDAY -> DaysOfTheWeek.MONDAY
            Calendar.TUESDAY -> DaysOfTheWeek.TUESDAY
            Calendar.WEDNESDAY -> DaysOfTheWeek.WEDNESDAY
            Calendar.THURSDAY -> DaysOfTheWeek.THURSDAY
            Calendar.FRIDAY -> DaysOfTheWeek.FRIDAY
            Calendar.SATURDAY -> DaysOfTheWeek.SATURDAY
            Calendar.SUNDAY -> DaysOfTheWeek.SUNDAY
            else -> null
        }
    }

    fun addSubject(
        name: String,
        professor: String,
        colorInt: Int,
        lectureDays: Set<DaysOfTheWeek>,
        dateStart: Long,
        dateEnd: Long
    ) {
        viewModelScope.launch {
            try {
                val newSubject = Subject(
                    name = name,
                    professor = professor,
                    colorInt = colorInt,
                    lectureDays = lectureDays,
                    dateStart = dateStart,
                    dateEnd = dateEnd
                )
                val id = subjectDao.insertSubjectWithId(newSubject)
                createLecturesForSubject(id, newSubject)
            } catch (err: Exception) {
                println("Erro em salvar: $err")
            }
        }
    }

    private suspend fun createLecturesForSubject(subjectId: Long, subject: Subject) {
        syncLecturesForSubject(subject.copy(id = subjectId))
    }

    fun deleteSubject(id: Long) {
        viewModelScope.launch { subjectDao.deleteSubject(id) }
    }


    fun updateSubject(subject: Subject) {
        viewModelScope.launch { subjectDao.updateSubject(subject) }
    }

    fun getSubjectById(id: Long): Flow<Subject?> = subjectDao.getSubjectById(id)

	fun updateLectureStatus(id: Long, status: LectureStatus) {
		viewModelScope.launch {
			lectureDao.updateLectureStatus(id, status)
		}
	}
}
//endregion

//region Assessments ViewModel
@HiltViewModel
class AssessmentsViewModel @Inject constructor(
    private val assessmentDao: AssessmentDao,
    private val subjectDao: SubjectDao
) : ViewModel() {

    val allAssessments: Flow<List<Assessment>> = assessmentDao.getAllAssessments()
	val getUpcomingAssessments: Flow<List<Assessment>> = assessmentDao.getUpcomingAssignments()
    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects()
    val allAssessmentsWithSubject: Flow<List<AssessmentWithSubject>> =
        assessmentDao.getAllAssessmentsWithSubject()

    fun addAssessment(
        name: String,
        subjectId: Long,
        date: Long,
        type: AssessmentType,
        weightPercentage: Double,
        maxScore: Double,
        score: Double? = null
    ) {
        viewModelScope.launch {
            try {
                val newAssessment = Assessment(
                    name = name,
                    subjectId = subjectId,
                    date = date,
                    type = type,
                    urgencyLevel = type.defaultUrgency,
                    weightPercentage = weightPercentage,
                    maxScore = maxScore,
                    score = score
                )
                assessmentDao.insertAssessment(newAssessment)
            } catch (err: Exception) {
                println("Erro ao salvar avaliação: $err")
            }
        }
    }

    fun deleteAssessment(id: Long) {
        viewModelScope.launch { assessmentDao.deleteAssessment(id) }
    }

    fun updateAssessment(assessment: Assessment) {
        viewModelScope.launch { assessmentDao.updateAssessment(assessment) }
    }

    fun getAssessmentById(id: Long): Flow<Assessment?> = assessmentDao.getAssessmentById(id)

    fun getAssessmentsBySubject(subjectId: Long): Flow<List<Assessment>> =
        assessmentDao.getAssessmentsBySubject(subjectId)
}
//endregion
