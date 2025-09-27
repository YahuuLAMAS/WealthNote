package com.example.upiexpensetracker.service

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

interface BiometricAuthService {
    fun isAvailable(context: Context): Boolean
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    )
}

class BiometricAuthServiceImpl : BiometricAuthService {
    
    override fun isAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    override fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val biometricPrompt = BiometricPrompt(activity, ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate to access UPI Expense Tracker")
            .setSubtitle("Use your biometric credential to unlock the app")
            .setNegativeButtonText("Cancel")
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
}

