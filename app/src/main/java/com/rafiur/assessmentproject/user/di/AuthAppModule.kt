package com.rafiur.assessmentproject.user.di

import com.google.firebase.auth.FirebaseAuth
import com.rafiur.assessmentproject.user.data.repository.FirebaseAuthRepository
import com.rafiur.assessmentproject.user.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthAppModule {
    @Provides
    fun provideAuthRepository(): AuthRepository {
        return FirebaseAuthRepository(FirebaseAuth.getInstance())
    }
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}