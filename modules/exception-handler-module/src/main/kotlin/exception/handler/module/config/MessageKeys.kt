package exception.handler.module.config

object MessageKeys {
    const val KEY_REGISTRATION_ERROR = "user.not.registered"
    const val KEY_REGISTRATION_ALREADY_DONE = "user.already.registered"
    const val KEY_TOKEN_AUTHENTICATION_ERROR = "token.authentication.error"
    const val KEY_USER_NOT_AUTHORIZED = "user.not.authorized"
    const val KEY_VALIDATION_ERROR = "parameters.errors"
    const val KEY_USER_DELETION_ERROR="user.not.deleted"
    const val KEY_REQUEST_BODY="request.body.required"
    const val KEY_USER_EMAIL_NOT_VERIFIED = "user.email.not.verified"
    const val KEY_EMAIL_MUST_BE_DISABLED="user.disabled.message"
    const val KEY_REFRESH_TOKEN_INVALID = "user.refresh.token.invalid"
}
