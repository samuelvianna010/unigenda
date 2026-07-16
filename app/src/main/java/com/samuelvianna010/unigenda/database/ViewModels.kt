package com.samuelvianna010.unigenda.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

//region Subject ViewModel
@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectDao: SubjectDao
) : ViewModel() {
    val allSubjects: Flow<List<Subject>> = subjectDao.getAllSubjects()

    fun addSubject(name: String, professor: String, colorInt: Int) {
        viewModelScope.launch {
            try {
                val newSubject = Subject(name = name, colorInt = colorInt, professor = professor)
                subjectDao.insertSubject(newSubject)
            } catch (err: Exception) {
                println("Erro em salvar: $err")
            }
        }
    }

    fun deleteSubject(id: Long) {
        viewModelScope.launch { subjectDao.deleteSubject(id) }
    }

    fun updateSubject(subject: Subject) {
        viewModelScope.launch { subjectDao.updateSubject(subject) }
    }

    fun getSubjectById(id: Long): Flow<Subject?> = subjectDao.getSubjectById(id)
}
//endregion

//region Assessments ViewModel
@HiltViewModel
class AssessmentsViewModel @Inject constructor(
    private val assessmentDao: AssessmentDao,
    private val subjectDao: SubjectDao
) : ViewModel() {

    val allAssessments: Flow<List<Assessment>> = assessmentDao.getAllAssessments()
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
