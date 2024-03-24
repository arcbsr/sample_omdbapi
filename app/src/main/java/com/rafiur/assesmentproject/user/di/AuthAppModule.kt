package com.rafiur.assesmentproject.user.di

import com.google.firebase.auth.FirebaseAuth
import com.rafiur.assesmentproject.user.data.repositoryimpl.FirebaseAuthRepository
import com.rafiur.assesmentproject.user.domain.repository.AuthRepository
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