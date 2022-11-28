package com.yeonkims.realnoteapp.data.exceptions

class LoginFailedException : Exception("Failed to log in. Please try again.")

class SignUpFailedException : Exception("Failed to sign up. Please try again.")

class ExistingEmailException : Exception("An account already exists with this email")

class DeleteAccountFailedException : Exception("Failed to delete this account")