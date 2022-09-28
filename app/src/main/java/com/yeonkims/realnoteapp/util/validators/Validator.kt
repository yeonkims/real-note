package com.yeonkims.realnoteapp.util.validators


fun List<Validator>.validate() : String? {
    return this.firstOrNull { validator ->
        !validator.isValid()
    }?.validate()
}

abstract class Validator(private val errorMessage: String) {
    abstract fun isValid() : Boolean

    fun validate() : String? {
        return if(isValid()) {
            null
        } else {
            errorMessage
        }
    }
}

class NonEmptyFieldsValidator(
    private val fields: List<String?>,
) : Validator("Please fill in all the blanks") {
    override fun isValid(): Boolean = fields.none { it.isNullOrBlank() }
}

class EmailValidator(
    private val email: String?
) : Validator("Please enter valid email") {
    override fun isValid() : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

class PasswordValidator(
    private val password: String?
) : Validator("Password must be at least 8 digits long") {
    override fun isValid(): Boolean = password != null && password.length >= 8
}

class MatchingPasswordsValidator(
    private val password: String?,
    private val passwordConfirmation: String?
) : Validator("Passwords do not match") {
    override fun isValid(): Boolean = password != null && password == passwordConfirmation
}
