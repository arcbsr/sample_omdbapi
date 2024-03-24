package com.rafiur.assesmentproject.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafiur.assesmentproject.user.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    fun checkCurrentUser() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.checkCurrentUser()
            if (result.isSuccess) {
                delay(500)
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.AuthInError("Please LogIn...")
            }
        }
    }

    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.AuthInError("Error in signIn")
            }
        }
    }

    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.AuthInError("Error in signUp")
            }
        }
    }

    fun signOut() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result.isSuccess) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.AuthInError("Error in signOut")
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    data class AuthInError(val msg: String) : AuthState()

}