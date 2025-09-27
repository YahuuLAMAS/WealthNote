# SMS Finance - UPI Expense Tracker

An Android application that automatically tracks UPI transactions by parsing SMS messages and provides expense management features.

## Features

- **Automatic SMS Parsing**: Automatically detects and parses UPI transaction SMS messages
- **Biometric Authentication**: Secure access using fingerprint or face recognition
- **Weekly Expense Tracking**: View total expenses for the current week
- **Transaction History**: Browse recent transactions with detailed information
- **Personal Notes**: Add custom notes to transactions
- **Modern UI**: Built with Jetpack Compose for a modern, responsive interface

## Architecture

The app follows Clean Architecture principles with the following layers:

### Data Layer
- **Models**: `Transaction` entity
- **DAO**: Room database access objects
- **Database**: Encrypted Room database with MasterKey

### Repository Layer
- **TransactionRepository**: Handles data operations and business logic

### Service Layer
- **SmsParsingService**: Parses SMS messages to extract transaction data
- **BiometricAuthService**: Handles biometric authentication

### Controller Layer
- **TransactionController**: Manages transaction-related operations
- **AuthenticationController**: Handles authentication logic

### ViewModel Layer
- **TransactionViewModel**: Manages transaction UI state
- **AuthenticationViewModel**: Manages authentication UI state

### UI Layer
- **Activities**: MainActivity with FragmentActivity support
- **Composables**: Modern UI built with Jetpack Compose
- **Screens**: AuthenticationScreen, MainScreen with transaction management

## Dependencies

- **AndroidX Core**: Core Android libraries
- **Jetpack Compose**: Modern UI toolkit
- **Room Database**: Local data persistence with encryption
- **Biometric**: Fingerprint and face authentication
- **Accompanist Permissions**: Runtime permission handling
- **Coroutines**: Asynchronous programming

## Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on Android device (API 24+)

## Permissions

- `READ_SMS`: Required to read incoming SMS messages
- `RECEIVE_SMS`: Required to receive SMS broadcasts
- `USE_BIOMETRIC`: Required for biometric authentication
- `USE_FINGERPRINT`: Required for fingerprint authentication

## Security

- Database encryption using AndroidX Security Crypto
- Biometric authentication for app access
- Secure SMS parsing without storing sensitive data

## SMS Parsing Patterns

The app supports multiple SMS patterns from various banks and UPI providers:

1. Standard bank debit/credit messages
2. UPI transaction notifications
3. Payment app notifications (GPay, PhonePe, etc.)
4. Generic UPI patterns

## Package Structure

```
com.example.upiexpensetracker/
├── activity/           # Activities
├── controller/         # Business logic controllers
├── data/              # Data layer
│   ├── dao/           # Database access objects
│   ├── database/      # Room database
│   └── model/         # Data models
├── receiver/          # Broadcast receivers
├── repository/        # Repository pattern implementation
├── service/           # Service layer
├── ui/               # UI layer
│   ├── screens/      # Composable screens
│   └── theme/        # UI theming
└── viewmodel/        # ViewModels
```

## License

This project is for educational purposes.

