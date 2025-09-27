package com.example.upiexpensetracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upiexpensetracker.controller.TransactionController
import com.example.upiexpensetracker.data.model.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionController: TransactionController
) : ViewModel() {
    
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions
    
    private val _weeklyExpense = MutableLiveData<Double>()
    val weeklyExpense: LiveData<Double> = _weeklyExpense
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        refreshTransactions()
    }
    
    fun refreshTransactions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val weeklyTransactions = transactionController.getWeeklyTransactions()
                _transactions.value = weeklyTransactions
                
                val totalExpense = transactionController.calculateWeeklyExpense()
                _weeklyExpense.value = totalExpense
            } catch (e: Exception) {
                // Handle error
                _transactions.value = emptyList()
                _weeklyExpense.value = 0.0
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                transactionController.addTransaction(transaction)
                refreshTransactions()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun updateTransactionNote(transaction: Transaction, note: String) {
        viewModelScope.launch {
            try {
                transactionController.updateTransactionNote(transaction, note)
                refreshTransactions()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    fun testSmsParsing(smsBody: String) {
        viewModelScope.launch {
            try {
                val success = transactionController.processIncomingSms(smsBody)
                if (success) {
                    refreshTransactions() // Refresh to show new transaction
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

