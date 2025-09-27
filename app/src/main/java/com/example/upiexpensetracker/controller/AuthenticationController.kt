package com.example.upiexpensetracker.controller

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.upiexpensetracker.service.BiometricAuthService

class AuthenticationController(
    private val biometricAuthService: BiometricAuthService
) {
    
    fun isBiometricAvailable(context: Context): Boolean {
        return biometricAuthService.isAvailable(context)
    }
    
    fun requestAuthentication(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        biometricAuthService.authenticate(activity, onSuccess, onError, onFailed)
    }
}

