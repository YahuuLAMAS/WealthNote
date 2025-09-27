package com.example.upiexpensetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.security.crypto.MasterKey
import com.example.upiexpensetracker.data.dao.TransactionDao
import com.example.upiexpensetracker.data.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    
    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null
        
        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

