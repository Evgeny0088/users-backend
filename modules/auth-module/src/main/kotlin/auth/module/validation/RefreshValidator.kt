package auth.module.validation

import auth.module.dto.RefreshRequest
import exception.handler.module.config.MessageKeys
import exception.handler.module.config.ValidationMessageResolver
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class RefreshValidator(
    private val messageResolver: ValidationMessageResolver
): Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return RefreshRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        if (target is RefreshRequest) {
            if (target.refreshToken.isNullOrBlank()) {
                errors.rejectValue(
                    "refreshToken",
                    "refresh.token.invalid",
                    messageResolver.getMessage(
                        messageKey = MessageKeys.KEY_REFRESH_TOKEN_INVALID,
                        args = arrayOf(target.username, target.refreshToken)))
            }
        }
    }
}
