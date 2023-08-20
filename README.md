# BankingAppJava
# Banking Application README

The **Banking Application** is a simple Java program that simulates basic banking functionalities. It allows users to create accounts, log in, and perform actions such as depositing and withdrawing money.

## Features

1. **Account Creation**: Users can create new banking accounts by providing their name. The application generates a unique account ID and a random 4-digit PIN for each account. The user's first name, last name, account ID, and PIN are displayed upon successful account creation.

2. **Account Authentication**: Users can log in to their accounts using their account ID and PIN. The application verifies the provided credentials and allows access to the account if they are correct.

3. **Balance Checking**: Upon logging in, users can view their current account balance.

4. **Depositing Money**: Users can deposit funds into their accounts. The updated account balance is displayed after each successful deposit.

5. **Withdrawing Money**: Users can withdraw funds from their accounts, provided their balance is sufficient. The application ensures that the withdrawal amount does not exceed the available balance.

6. **Data Persistence**: Account information is saved to and loaded from the "accounts.dat" file. This allows users to access their accounts and their balances across different sessions of using the application.

## How to Use

1. **Compilation**: Compile the Java source code using the Java compiler.

2. **Execution**: Run the compiled Java program.


3. **Menu Options**:
- Choose option 1 to log in if you already have an account.
- Choose option 2 to create a new account. Enter your name, and the program will provide you with your account ID and PIN.
- After account creation or logging in, you can choose to deposit, withdraw, or log out using the given options.

4. **Exiting**: The application will prompt you to save your accounts when exiting.

## Note

- This application is for educational purposes and demonstrates simple banking functionalities. It is not intended for actual use in a banking environment.
- The security measures used in the application (e.g., PIN generation, PIN hashing) are basic and not suitable for production-level security.
- The application uses the "accounts.dat" file to store account data. Ensure that this file is present in the same directory as the program.

## Contributors

This application was developed by Rajarshi Ghosh.



