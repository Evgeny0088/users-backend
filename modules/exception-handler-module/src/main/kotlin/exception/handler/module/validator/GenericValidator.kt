package exception.handler.module.validator

import exception.handler.module.config.MessageKeys
import exception.handler.module.config.MessageResolver
import exception.handler.module.enum.ErrorCode
import exception.handler.module.exception.ValidationException
import org.apache.commons.validator.routines.EmailValidator
import java.util.Locale
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

interface GenericValidator<T> {

    fun validate(obj: T, locale: Locale)

    fun validateStringFields(obj: T, messageResolver: MessageResolver, locale: Locale) {
        val errors = HashMap<String, String>()

        obj!!::class.memberProperties.forEach { property ->
            val value = this.getValue(property, obj)?.let { it as String }
            this.nullValueCheck(value, property, errors)
        }

        if (errors.isNotEmpty()) {
            throw ValidationException(
                errorMessage = messageResolver.getErrorMessage(MessageKeys.KEY_VALIDATION_ERROR, locale),
                businessCode = ErrorCode.INPUT_BAD_REQUEST,
                details = errors
            )
        }
    }

    fun nullValueCheck(value: String?, property: KProperty1<out T, *>, errors: HashMap<String, String>) {
        if (value.isNullOrEmpty()) {
            errors.putIfAbsent(property.name, "Must not be empty or null.")
        }
    }

    fun getValue(property: KProperty1<out T, *>, obj: T): Any? = property.getter.call(obj)

    fun validateEmail(email: String): Boolean = EmailValidator.getInstance().isValid(email)
}