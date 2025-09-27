package com.example.upiexpensetracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.example.upiexpensetracker.controller.TransactionController
import com.example.upiexpensetracker.data.database.TransactionDatabase
import com.example.upiexpensetracker.repository.TransactionRepository
import com.example.upiexpensetracker.service.SmsParsingServiceImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            
            // Initialize services and controllers
            val database = TransactionDatabase.getDatabase(context)
            val repository = TransactionRepository(database.transactionDao())
            val smsParsingService = SmsParsingServiceImpl()
            val transactionController = TransactionController(repository, smsParsingService)
            
            for (message in messages) {
                val smsBody = message.messageBody
                
                // Use coroutine to process SMS
                GlobalScope.launch {
                    transactionController.processIncomingSms(smsBody)
                }
            }
        }
    }
}

