package com.example.upiexpensetracker.repository

import com.example.upiexpensetracker.data.dao.TransactionDao
import com.example.upiexpensetracker.data.model.Transaction

interface TransactionRepositoryInterface {
    suspend fun getWeeklyTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun getAllTransactions(): List<Transaction>
}

class TransactionRepository(
    private val transactionDao: TransactionDao
) : TransactionRepositoryInterface {
    
    override suspend fun getWeeklyTransactions(): List<Transaction> {
        val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        return transactionDao.getTransactionsFromDate(weekAgo)
    }
    
    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
    
    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }
    
    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionDao.getAllTransactions()
    }
}

