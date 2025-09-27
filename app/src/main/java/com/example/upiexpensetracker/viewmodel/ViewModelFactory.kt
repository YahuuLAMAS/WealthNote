package com.example.upiexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.upiexpensetracker.controller.AuthenticationController
import com.example.upiexpensetracker.controller.TransactionController

class TransactionViewModelFactory(
    private val transactionController: TransactionController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(transactionController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AuthenticationViewModelFactory(
    private val authController: AuthenticationController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(authController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

