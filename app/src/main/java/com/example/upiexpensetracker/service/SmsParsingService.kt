package com.example.upiexpensetracker.service

import com.example.upiexpensetracker.data.model.Transaction
import java.util.regex.Pattern

interface SmsParsingService {
    fun parseTransaction(smsBody: String): Transaction?
}

class SmsParsingServiceImpl : SmsParsingService {
    
    private val patterns = listOf(
        // Pattern 1: "Your a/c XXXX is debited for Rs. 250.00 on 25-09-2025 and credited to VPA MERCHANT.XYZ@bank."
        Pattern.compile(
            "Your a/c \\w+ is (debited|credited) for Rs\\. ([0-9,]+\\.\\d{2}).*?(?:to|from) (.+?)(?:\\.|$)",
            Pattern.CASE_INSENSITIVE
        ),
        
        // Pattern 2: "Transaction of INR 75.50 has been made to SOME_STORE from your account."
        Pattern.compile(
            "Transaction of INR ([0-9,]+\\.\\d{2}) has been made to (.+?) from",
            Pattern.CASE_INSENSITIVE
        ),
        
        // Pattern 3: "You have spent Rs. 120.00 on GPay at ANOTHER_VENDOR."
        Pattern.compile(
            "You have spent Rs\\. ([0-9,]+\\.\\d{2}) on \\w+ at (.+?)(?:\\.|$)",
            Pattern.CASE_INSENSITIVE
        ),
        
        // Pattern 4: "Rs. 500.00 credited to your account no. XXXX via UPI."
        Pattern.compile(
            "Rs\\. ([0-9,]+\\.\\d{2}) (credited|debited) (?:to|from) your account.*?(?:via|from|to) (.+?)(?:\\.|$)",
            Pattern.CASE_INSENSITIVE
        ),
        
        // Generic UPI pattern
        Pattern.compile(
            "(?:Rs\\.|INR) ?([0-9,]+\\.\\d{2}).*?(debited|credited).*?(?:to|from|at) (.+?)(?:\\.|,|$)",
            Pattern.CASE_INSENSITIVE
        )
    )
    
    override fun parseTransaction(smsBody: String): Transaction? {
        val currentTime = System.currentTimeMillis()
        
        for (pattern in patterns) {
            val matcher = pattern.matcher(smsBody)
            if (matcher.find()) {
                try {
                    when (pattern) {
                        patterns[0] -> {
                            val type = matcher.group(1)?.lowercase() ?: ""
                            val amount = matcher.group(2)?.replace(",", "")?.toDouble() ?: 0.0
                            val merchant = matcher.group(3)?.trim() ?: "Unknown"
                            return Transaction(
                                timestamp = currentTime,
                                amount = amount,
                                merchant = merchant,
                                isDebit = type == "debited"
                            )
                        }
                        patterns[1] -> {
                            val amount = matcher.group(1)?.replace(",", "")?.toDouble() ?: 0.0
                            val merchant = matcher.group(2)?.trim() ?: "Unknown"
                            return Transaction(
                                timestamp = currentTime,
                                amount = amount,
                                merchant = merchant,
                                isDebit = true // Transaction "made to" implies debit
                            )
                        }
                        patterns[2] -> {
                            val amount = matcher.group(1)?.replace(",", "")?.toDouble() ?: 0.0
                            val merchant = matcher.group(2)?.trim() ?: "Unknown"
                            return Transaction(
                                timestamp = currentTime,
                                amount = amount,
                                merchant = merchant,
                                isDebit = true // "spent" implies debit
                            )
                        }
                        patterns[3] -> {
                            val amount = matcher.group(1)?.replace(",", "")?.toDouble() ?: 0.0
                            val type = matcher.group(2)?.lowercase() ?: ""
                            val merchant = matcher.group(3)?.trim() ?: "Unknown"
                            return Transaction(
                                timestamp = currentTime,
                                amount = amount,
                                merchant = merchant,
                                isDebit = type == "debited"
                            )
                        }
                        patterns[4] -> {
                            val amount = matcher.group(1)?.replace(",", "")?.toDouble() ?: 0.0
                            val type = matcher.group(2)?.lowercase() ?: ""
                            val merchant = matcher.group(3)?.trim() ?: "Unknown"
                            return Transaction(
                                timestamp = currentTime,
                                amount = amount,
                                merchant = merchant,
                                isDebit = type == "debited"
                            )
                        }
                    }
                } catch (e: Exception) {
                    continue
                }
            }
        }
        return null
    }
}

