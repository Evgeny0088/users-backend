package email.service.module.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import service.config.module.Constants.BEAN_MAIL_TEMPLATES
import java.util.*

@Component
class EmailMessageResolver(
    @Qualifier(BEAN_MAIL_TEMPLATES)
    private val messageResolver: MessageSource
) {

    fun getMessage(messageKey: String, vararg args: String? = arrayOfNulls(1)): String {
        val locale = LocaleContextHolder.getLocale()
        return messageResolver.getMessage(
            messageKey, args, locale ?: Locale.getDefault()
        )
    }
}
