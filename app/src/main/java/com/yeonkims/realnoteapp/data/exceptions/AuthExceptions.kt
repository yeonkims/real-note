package com.yeonkims.realnoteapp.data.exceptions

class LoginFailedException : Exception("Failed to log in")

class SignUpFailedException : Exception("Failed to sign up")

class ExistingEmailException : Exception("An account already exists with this email")

class DeleteAccountFailedException : Exception("Failed to delete this account")