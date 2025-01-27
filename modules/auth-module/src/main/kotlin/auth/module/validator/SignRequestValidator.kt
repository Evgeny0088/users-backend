package auth.module.validator

import auth.module.dto.SignUpRequest
import exception.handler.module.config.MessageKeys.KEY_VALIDATION_ERROR
import exception.handler.module.config.MessageResolver
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.ValidationException
import exception.handler.module.validator.GenericValidator
import org.springframework.stereotype.Component
import java.util.*
import kotlin.reflect.full.memberProperties

@Component
class SignRequestValidator(private val messageResolver: MessageResolver): GenericValidator<SignUpRequest> {

    override fun validate(obj: SignUpRequest, locale: Locale) {
        val errors = HashMap<String, String>()

        obj::class.memberProperties.forEach { property ->
            if (property.name != "role" && property.name != "isEnabled") {
                val value = this.getValue(property, obj)?.let { it as String }
                this.nullValueCheck(value, property, errors)
                if (property.name == "email") {
                    value?.let {
                        if (!this.validateEmail(it)) {
                            errors.putIfAbsent(property.name, "Email must comply with email pattern.")
                        }
                    }
                }
            }
        }
        if (errors.isNotEmpty()) {
            throw ValidationException(
                errorMessage = messageResolver.getErrorMessage(KEY_VALIDATION_ERROR, locale),
                businessCode = ErrorCode.INPUT_BAD_REQUEST,
                details = errors
            )
        }
    }
}