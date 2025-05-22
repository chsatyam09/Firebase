package com.example.firebaselearning.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaselearning.domain.repository.AuthOutcome
import com.example.firebaselearning.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = repository.loginUser(email, password)) {
                is AuthOutcome.Success -> _authState.value = AuthState.Success
                is AuthOutcome.Failure -> _authState.value = AuthState.Failure(result.message)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = repository.registerUser(email, password)) {
                is AuthOutcome.Success -> _authState.value = AuthState.Success
                is AuthOutcome.Failure -> _authState.value = AuthState.Failure(result.message)
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
