package com.example.upiexpensetracker.data.dao

import androidx.room.*
import com.example.upiexpensetracker.data.model.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    suspend fun getTransactionsFromDate(startTime: Long): List<Transaction>
    
    @Insert
    suspend fun insertTransaction(transaction: Transaction)
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>
}

