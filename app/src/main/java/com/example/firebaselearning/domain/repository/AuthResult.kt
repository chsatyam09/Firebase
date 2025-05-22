package com.example.firebaselearning.domain.repository


sealed class AuthOutcome<out T> {
    data class Success<out T>(val data: T) : AuthOutcome<T>()
    data class Failure(val message: String) : AuthOutcome<Nothing>()
}
