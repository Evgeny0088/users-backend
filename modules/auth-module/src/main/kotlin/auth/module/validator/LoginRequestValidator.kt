package auth.module.validator

import auth.module.dto.LoginRequest
import exception.handler.module.config.MessageResolver
import exception.handler.module.validator.GenericValidator
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoginRequestValidator(private val messageResolver: MessageResolver): GenericValidator<LoginRequest> {

    override fun validate(obj: LoginRequest, locale: Locale) {
        super.validateStringFields(obj, messageResolver, locale)
    }
}