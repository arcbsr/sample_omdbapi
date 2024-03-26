package com.rafiur.assessmentproject.user.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.rafiur.assessmentproject.user.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkCurrentUser(): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Result.success(Unit)
            } else {
                Result.failure(Throwable("Unknown"))
            }
        } catch (e: Exception) {
            // Handle exception
            Result.failure(e)
        }

    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}