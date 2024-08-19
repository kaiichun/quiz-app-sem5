package com.alvin.quiz.core.di.module

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.data.repository.QuestionRepository
import com.alvin.quiz.data.repository.QuizRepository
import com.alvin.quiz.data.repository.StudentQuizCompletionRepository
import com.alvin.quiz.data.repository.StudentResultRepository
import com.alvin.quiz.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule{
    @Provides
    @Singleton
    fun provideQuestionRepository(authService: AuthService): QuestionRepository {
        return QuestionRepository(authService)
    }
    @Provides
    @Singleton
    fun provideQuizRepository(authService: AuthService): QuizRepository {
        return QuizRepository(authService)
    }
    @Provides
    @Singleton
    fun provideStudentQuizCompletionRepository(authService: AuthService): StudentQuizCompletionRepository {
        return StudentQuizCompletionRepository(authService)
    }
    @Provides
    @Singleton
    fun provideStudentResultRepository(authService: AuthService): StudentResultRepository {
        return StudentResultRepository(authService)
    }
    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository {
        return UserRepository(authService)
    }
}