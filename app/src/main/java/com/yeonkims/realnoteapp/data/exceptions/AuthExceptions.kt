package com.yeonkims.realnoteapp.data.exceptions

import java.lang.Exception

class LoginFailedException : Exception("Failed to log in")

class SignUpFailedException : Exception("Failed to sign up")

class ExistingEmailException : Exception("An account already exists with this email")