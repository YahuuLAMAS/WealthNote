package com.example.upiexpensetracker.ui

import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.FragmentActivity
import com.example.upiexpensetracker.ui.screens.AuthenticationScreen
import com.example.upiexpensetracker.ui.screens.MainScreen
import com.example.upiexpensetracker.viewmodel.AuthenticationViewModel
import com.example.upiexpensetracker.viewmodel.TransactionViewModel

@Composable
fun AppContent(
    transactionViewModel: TransactionViewModel,
    authViewModel: AuthenticationViewModel,
    activity: FragmentActivity
) {
    val isAuthenticated by authViewModel.isAuthenticated.observeAsState(false)
    val authError by authViewModel.authError.observeAsState()
    
    LaunchedEffect(Unit) {
        // FOR TESTING: Always skip authentication
        // TODO: Remove this in production
        authViewModel.setAuthenticated(true)
    }
    
    // FORCE AUTHENTICATION BYPASS FOR TESTING
    MainScreen(viewModel = transactionViewModel)
}

