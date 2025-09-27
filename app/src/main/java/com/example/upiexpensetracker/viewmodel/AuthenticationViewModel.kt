package com.example.upiexpensetracker.viewmodel

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.upiexpensetracker.controller.AuthenticationController

class AuthenticationViewModel(
    private val authController: AuthenticationController
) : ViewModel() {
    
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated
    
    private val _authError = MutableLiveData<String?>()
    val authError: LiveData<String?> = _authError
    
    init {
        _isAuthenticated.value = false
    }
    
    fun checkBiometricAvailability(context: Context): Boolean {
        return authController.isBiometricAvailable(context)
    }
    
    fun requestAuthentication(activity: FragmentActivity) {
        authController.requestAuthentication(
            activity = activity,
            onSuccess = {
                _isAuthenticated.value = true
                _authError.value = null
            },
            onError = { error ->
                _isAuthenticated.value = false
                _authError.value = error
            },
            onFailed = {
                _isAuthenticated.value = false
                _authError.value = "Authentication failed"
            }
        )
    }
    
    fun setAuthenticated(authenticated: Boolean) {
        _isAuthenticated.value = authenticated
        if (authenticated) {
            _authError.value = null
        }
    }
}

