package com.example.firebaselearning.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaselearning.domain.repository.AuthOutcome
import com.example.firebaselearning.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _authState.value = AuthState.Loading
//            when (val result = repository.loginUser(email, password)) {
//                is AuthOutcome.Success -> _authState.value = AuthState.Success
//                is AuthOutcome.Failure -> _authState.value = AuthState.Failure(result.message)
//            }
//        }
//    }

//    fun register(email: String, password: String) {
//        viewModelScope.launch {
//            _authState.value = AuthState.Loading
//            when (val result = repository.registerUser(email, password)) {
//                is AuthOutcome.Success -> _authState.value = AuthState.Success
//                is AuthOutcome.Failure -> _authState.value = AuthState.Failure(result.message)
//            }
//        }
//    }

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user

                if (user != null && user.isEmailVerified) {
                    _authState.value = AuthState.Success
                } else {
                    auth.signOut()
                    _authState.value = AuthState.Failure("Please verify your email before logging in.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Failure(e.message ?: "Login failed")
            }
        }
    }


    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.sendEmailVerification()?.await()

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Failure(e.message ?: "Unknown error")
            }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Failure(e.message ?: "Failed to send reset email")
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            try {
                val result = auth.signInWithCredential(credential).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Failure(e.message ?: "Google Sign-In failed")
            }
        }
    }



    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
