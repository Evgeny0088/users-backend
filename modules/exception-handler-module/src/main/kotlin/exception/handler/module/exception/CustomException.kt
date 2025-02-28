package exception.handler.module.exception

import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys
import exception.handler.module.enum.ErrorCode
import org.springframework.http.HttpStatus

open class CustomException: RuntimeException {

    var errorMessage: String
    var httpStatus: Int = 0
    var businessCode: ErrorCode? = null
    var details: Any? = null

    constructor(errorMessage: String) : super(errorMessage) {
        this.errorMessage = errorMessage
    }

    constructor(errorMessage: String, httpStatus: Int) : super(errorMessage) {
        this.errorMessage = errorMessage
        this.httpStatus = httpStatus
    }

    constructor(
        errorMessage: String,
        httpStatus: Int,
        businessCode: ErrorCode
    ) : super(errorMessage) {
        this.errorMessage = errorMessage
        this.httpStatus = httpStatus
        this.businessCode = businessCode
        this.businessCode?.setHttpCode(httpStatus)
    }

    companion object {
        @JvmStatic
        fun defaultUnauthorizedException(
            messageResolver: ErrorsMessageResolver,
            vararg args: String? = arrayOfNulls(0)): CustomException {
            return CustomException(
                errorMessage = messageResolver.getMessage(
                    MessageKeys.KEY_TOKEN_AUTHENTICATION_ERROR, *args),
                httpStatus = HttpStatus.UNAUTHORIZED.value(),
                businessCode = ErrorCode.TOKEN_ERROR_401
            )
        }

        fun defaultAccessDeniedException(messageResolver: ErrorsMessageResolver, vararg args: String? = arrayOfNulls(0)): CustomException {
            return CustomException(
                errorMessage = messageResolver.getMessage(MessageKeys.KEY_USER_NOT_AUTHORIZED, *args),
                httpStatus = HttpStatus.FORBIDDEN.value(),
                businessCode = ErrorCode.USER_IS_FORBIDDEN
            )
        }

        fun defaultEmailVerificationException(messageResolver: ErrorsMessageResolver, vararg args: String? = arrayOfNulls(0)): CustomException {
            return CustomException(
                errorMessage = messageResolver.getMessage(MessageKeys.KEY_USER_EMAIL_NOT_VERIFIED, *args),
                httpStatus = HttpStatus.UNAUTHORIZED.value(),
                businessCode = ErrorCode.USER_EMAIL_NOT_VERIFIED
            )
        }
    }
}
