package com.example.firebaselearning.viewmodal

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Failure(val message: String) : AuthState()
}
