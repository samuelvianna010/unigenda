package com.samuelvianna010.unigenda.di

import android.content.Context
import androidx.room.Room
import com.samuelvianna010.unigenda.database.AppDatabase
import com.samuelvianna010.unigenda.database.SubjectDao
import com.samuelvianna010.unigenda.database.AssessmentDao
import com.samuelvianna010.unigenda.database.LectureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//region Database Module
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //region Database Provider
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "unigenda_db"
        ).fallbackToDestructiveMigration().build()
    }
    //endregion

    //region DAO Providers
    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    fun provideAssessmentDao(database: AppDatabase): AssessmentDao {
        return database.assessmentDao()
    }

    @Provides
    fun provideLectureDao(database: AppDatabase): LectureDao {
        return database.lectureDao()
    }
    //endregion
}
//endregion
