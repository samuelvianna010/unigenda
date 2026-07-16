package com.samuelvianna010.unigenda.di

import android.content.Context
import androidx.room.Room
import com.samuelvianna010.unigenda.database.AppDatabase
import com.samuelvianna010.unigenda.database.SubjectDao
import com.samuelvianna010.unigenda.database.AssessmentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "unigenda_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSubjectDao(database: AppDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    fun provideAssessmentDao(database: AppDatabase): AssessmentDao {
        return database.assessmentDao()
    }
}
