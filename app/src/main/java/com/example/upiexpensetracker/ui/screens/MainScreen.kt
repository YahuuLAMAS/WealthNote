package com.example.upiexpensetracker.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.upiexpensetracker.data.model.Transaction
import com.example.upiexpensetracker.util.AppConfig
import com.example.upiexpensetracker.viewmodel.TransactionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(viewModel: TransactionViewModel) {
    val smsPermissionState = rememberPermissionState(Manifest.permission.READ_SMS)
    val transactions by viewModel.transactions.observeAsState(emptyList())
    val weeklyExpense by viewModel.weeklyExpense.observeAsState(0.0)
    val isLoading by viewModel.isLoading.observeAsState(false)
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    
    LaunchedEffect(Unit) {
        viewModel.refreshTransactions()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // App Title
        Text(
            text = "UPI Expense Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Test SMS Parsing Button (Debug builds only)
        if (AppConfig.SHOW_TEST_BUTTONS) {
            Button(
                onClick = {
                    // Test SMS parsing with sample message
                    val testSms = "Your a/c XXXX is debited for Rs. 250.00 on 25-09-2025 and credited to VPA MERCHANT.XYZ@bank."
                    viewModel.testSmsParsing(testSms)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Test SMS Parsing (Debug)")
            }
        }
        
        // SMS Permission Check
        if (!smsPermissionState.status.isGranted) {
            PermissionCard(
                onRequestPermission = { smsPermissionState.launchPermissionRequest() }
            )
        }
        
        // Weekly Expense Summary
        WeeklyExpenseSummary(weeklyExpense = weeklyExpense)
        
        // Transactions List
        TransactionsList(
            transactions = transactions,
            isLoading = isLoading,
            onTransactionClick = { selectedTransaction = it }
        )
    }
    
    // Transaction Note Dialog
    selectedTransaction?.let { transaction ->
        TransactionNoteDialog(
            transaction = transaction,
            onDismiss = { selectedTransaction = null },
            onSave = { note ->
                viewModel.updateTransactionNote(transaction, note)
                selectedTransaction = null
            }
        )
    }
}

@Composable
fun PermissionCard(onRequestPermission: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "SMS Permission Required",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "This app needs SMS permission to automatically track your UPI transactions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Grant SMS Permission")
            }
        }
    }
}

@Composable
fun WeeklyExpenseSummary(weeklyExpense: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Total Expenses This Week",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "₹${String.format("%.2f", weeklyExpense)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}

@Composable
fun TransactionsList(
    transactions: List<Transaction>,
    isLoading: Boolean,
    onTransactionClick: (Transaction) -> Unit
) {
    Text(
        text = "Recent Transactions (Last 7 Days)",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    
    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        transactions.isEmpty() -> {
            EmptyTransactionsCard()
        }
        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyTransactionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No transactions recorded in the last 7 days.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(transaction.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = transaction.merchant,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (!transaction.userNote.isNullOrBlank()) {
                    Text(
                        text = transaction.userNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Text(
                text = "${if (transaction.isDebit) "-" else "+"}₹${String.format("%.2f", transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.isDebit) Color.Red else Color.Green
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionNoteDialog(
    transaction: Transaction,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var note by remember { mutableStateOf(transaction.userNote ?: "") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add Note",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${transaction.merchant} - ₹${String.format("%.2f", transaction.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Personal Note") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(note) }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

