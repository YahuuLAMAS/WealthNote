package com.example.upiexpensetracker.controller

import com.example.upiexpensetracker.data.model.Transaction
import com.example.upiexpensetracker.repository.TransactionRepositoryInterface
import com.example.upiexpensetracker.service.SmsParsingService

class TransactionController(
    private val repository: TransactionRepositoryInterface,
    private val smsParsingService: SmsParsingService
) {
    
    suspend fun getWeeklyTransactions(): List<Transaction> {
        return repository.getWeeklyTransactions()
    }
    
    suspend fun addTransaction(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
    
    suspend fun updateTransactionNote(transaction: Transaction, note: String) {
        val updatedTransaction = transaction.copy(userNote = note)
        repository.updateTransaction(updatedTransaction)
    }
    
    suspend fun processIncomingSms(smsBody: String): Boolean {
        val transaction = smsParsingService.parseTransaction(smsBody)
        return if (transaction != null) {
            repository.insertTransaction(transaction)
            true
        } else {
            false
        }
    }
    
    suspend fun calculateWeeklyExpense(): Double {
        val transactions = repository.getWeeklyTransactions()
        return transactions.filter { it.isDebit }.sumOf { it.amount }
    }
}

