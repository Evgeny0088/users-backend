package email.service.module.service

import email.service.module.config.Constants.IMAGE_CID
import email.service.module.config.Constants.IMAGE_STATIC_EMAIL
import email.service.module.dto.EmailDto
import email.service.module.config.EmailMessageResolver
import email.service.module.config.MailProperties
import email.service.module.config.MessageKey.KEY_EMAIL_SUBJECT
import jakarta.mail.internet.MimeMessage
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    private val messageResolver: EmailMessageResolver,
    private val templateEngine: ISpringWebFluxTemplateEngine,
    private val mailProperties: MailProperties) {

    suspend fun sendEmail(emailDto: EmailDto) {
        val ctx = Context(LocaleContextHolder.getLocale() ?: Locale.getDefault())
            .also {
                it.setVariable(emailDto::class.java.simpleName, emailDto)
                it.setVariable(IMAGE_CID, IMAGE_STATIC_EMAIL)
            }

        val htmlContent: String = this.templateEngine.process("registration.html", ctx)

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name())
            .also {
                it.setSubject(messageResolver.getMessage(KEY_EMAIL_SUBJECT))
                it.setFrom(mailProperties.username)
                it.setText(htmlContent, true)
                it.setTo(mailProperties.sendTo)
                it.addInline(IMAGE_STATIC_EMAIL, ClassPathResource("static/penguin.png"))
            }
        mailSender.send(mimeMessage)
    }
}
