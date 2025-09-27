package com.example.upiexpensetracker.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.upiexpensetracker.controller.AuthenticationController
import com.example.upiexpensetracker.controller.TransactionController
import com.example.upiexpensetracker.data.database.TransactionDatabase
import com.example.upiexpensetracker.repository.TransactionRepository
import com.example.upiexpensetracker.service.BiometricAuthServiceImpl
import com.example.upiexpensetracker.service.SmsParsingServiceImpl
import com.example.upiexpensetracker.ui.AppContent
import com.example.upiexpensetracker.ui.theme.UpiExpenseTrackerTheme
import com.example.upiexpensetracker.viewmodel.AuthenticationViewModel
import com.example.upiexpensetracker.viewmodel.TransactionViewModel
import com.example.upiexpensetracker.viewmodel.AuthenticationViewModelFactory
import com.example.upiexpensetracker.viewmodel.TransactionViewModelFactory

class MainActivity : FragmentActivity() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var authViewModel: AuthenticationViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initializeServices()
        
        setContent {
            UpiExpenseTrackerTheme {
                AppContent(
                    transactionViewModel = transactionViewModel,
                    authViewModel = authViewModel,
                    activity = this@MainActivity
                )
            }
        }
    }
    
    private fun initializeServices() {
        // Initialize database and repository
        val database = TransactionDatabase.getDatabase(this)
        val repository = TransactionRepository(database.transactionDao())
        
        // Initialize services
        val smsParsingService = SmsParsingServiceImpl()
        val biometricAuthService = BiometricAuthServiceImpl()
        
        // Initialize controllers
        val transactionController = TransactionController(repository, smsParsingService)
        val authController = AuthenticationController(biometricAuthService)
        
        // Initialize ViewModels
        val transactionViewModelFactory = TransactionViewModelFactory(transactionController)
        transactionViewModel = ViewModelProvider(this, transactionViewModelFactory)[TransactionViewModel::class.java]
        
        val authViewModelFactory = AuthenticationViewModelFactory(authController)
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthenticationViewModel::class.java]
    }
}

