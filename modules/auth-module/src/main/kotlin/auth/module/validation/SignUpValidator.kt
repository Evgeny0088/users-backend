package auth.module.validation

import auth.module.dto.SignUpRequest
import exception.handler.module.config.MessageKeys
import exception.handler.module.config.ValidationMessageResolver
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class SignUpValidator(
    private val messageResolver: ValidationMessageResolver
): Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return SignUpRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        if (target is SignUpRequest) {
            if (target.isEnabled) {
                errors.rejectValue(
                    "isEnabled",
                    "signed.user.disabled",
                    messageResolver.getMessage(MessageKeys.KEY_EMAIL_MUST_BE_DISABLED))
            }
        }
    }
}
