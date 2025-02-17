package exception.handler.module.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import service.config.module.Constants.BEAN_BUSINESS_ERRORS
import java.util.*

@Component
class ErrorsMessageResolver (
    @Qualifier(BEAN_BUSINESS_ERRORS)
    private val messageResolver: MessageSource
) {

    fun getMessage(messageKey: String, vararg args: String? = arrayOfNulls(1)): String {
        val locale = LocaleContextHolder.getLocale()
        return messageResolver.getMessage(
            messageKey, args, locale ?: Locale.getDefault()
        )
    }
}