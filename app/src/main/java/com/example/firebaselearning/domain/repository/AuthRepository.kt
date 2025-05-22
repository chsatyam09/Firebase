package com.example.firebaselearning.domain.repository

import kotlinx.coroutines.tasks.await
import java.lang.Exception
import com.google.firebase.auth.FirebaseAuth

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun loginUser(email: String, password: String): AuthOutcome<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthOutcome.Success(Unit)
        } catch (e: Exception) {
            AuthOutcome.Failure(e.message ?: "Login failed due to an unknown error.")
        }
    }

    suspend fun registerUser(email: String, password: String): AuthOutcome<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthOutcome.Success(Unit)
        } catch (e: Exception) {
            AuthOutcome.Failure(e.message ?: "Registration failed due to an unknown error.")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser() = firebaseAuth.currentUser
}
