package com.example.upiexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long,
    val amount: Double,
    val merchant: String,
    val isDebit: Boolean,
    val userNote: String? = null
)

