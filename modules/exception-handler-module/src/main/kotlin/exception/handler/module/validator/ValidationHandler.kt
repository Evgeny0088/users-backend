package exception.handler.module.validator

import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.config.MessageKeys
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.ValidationException
import org.apache.commons.lang3.ObjectUtils
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest

class ValidationHandler<V: Validator>(
    validator: V,
    private val messageResolver: ErrorsMessageResolver) {

    private val validators: MutableList<Validator> = mutableListOf()

    init {
        validators.add(validator)
    }

    private fun onValidationErrors(
        errors: Errors,
        invalidBody: Any? = null,
        request: ServerRequest? = null
    ): Throwable {
        throw ValidationException(
            errorMessage = messageResolver.getMessage(MessageKeys.KEY_VALIDATION_ERROR),
            businessCode = ErrorCode.INPUT_BAD_REQUEST,
            details = mapErrors(errors)
        )
    }

    fun handleValidation(target: Any, request: ServerRequest): Any {
        val errors = BeanPropertyBindingResult(target, target::class.java.name)
        validators.forEach { it.validate(target, errors) }
        if (ObjectUtils.isNotEmpty(target) && errors.fieldErrors.isNotEmpty()) {
            throw onValidationErrors(errors)
        }
        return target
    }

    fun assignValidator(validator: V) {
        validators.add(validator)
    }

    private fun mapErrors(errors: Errors): Map<String, String> {
        return errors.fieldErrors.associateBy( { it.field }, { it.defaultMessage ?: "Validation error." })
    }
}