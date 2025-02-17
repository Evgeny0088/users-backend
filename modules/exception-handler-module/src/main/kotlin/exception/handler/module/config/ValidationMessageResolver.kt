package exception.handler.module.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import service.config.module.Constants.BEAN_BUSINESS_ERRORS
import service.config.module.Constants.BEAN_VALIDATION_ERRORS
import java.util.*

@Component
class ValidationMessageResolver (
    @Qualifier(BEAN_VALIDATION_ERRORS)
    private val messageResolver: MessageSource
) {

    fun getMessage(messageKey: String, vararg args: String? = arrayOfNulls(1)): String {
        return messageResolver.getMessage(
            messageKey, args, LocaleContextHolder.getLocale() ?: Locale.getDefault()
        )
    }
}