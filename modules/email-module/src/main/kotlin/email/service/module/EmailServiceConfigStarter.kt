package email.service.module

import email.service.module.config.Constants.MAIL_SMTP_AUTH
import email.service.module.config.Constants.MAIL_SMTP_DEBUG
import email.service.module.config.Constants.MAIL_SMTP_PROTOCOL
import email.service.module.config.Constants.MAIL_SMTP_SSL
import email.service.module.config.EmailMessageResolver
import email.service.module.config.MailProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.thymeleaf.spring6.ISpringWebFluxTemplateEngine
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import service.config.module.Constants.BEAN_MAIL_TEMPLATES
import service.config.module.ServiceConfigStarter
import service.config.module.utils.ServiceUtils.messageSourceCreator
import java.nio.charset.StandardCharsets

@ComponentScan
@Configuration(proxyBeanMethods = false)
@Import(ServiceConfigStarter::class)
@EnableConfigurationProperties(MailProperties::class)
class EmailServiceConfigStarter {

    @Bean(BEAN_MAIL_TEMPLATES)
    fun emailMessageSource(): MessageSource {
        return messageSourceCreator("email/templates")
    }

    @Bean
    @Primary
    fun thymeleafTemplateResolver(): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = "email/html-templates/"
            suffix = ".html"
            templateMode = TemplateMode.HTML
            order = 1
            characterEncoding = StandardCharsets.UTF_8.name()
        }
    }

    @Bean
    @Primary
    fun templatesEngine(
        @Qualifier(BEAN_MAIL_TEMPLATES) messageSource: MessageSource,
        templateResolver: ITemplateResolver
    ): ISpringWebFluxTemplateEngine {
        return SpringWebFluxTemplateEngine().also {
            it.addTemplateResolver(templateResolver)
            it.setTemplateEngineMessageSource(messageSource)
        }
    }

    @Bean
    fun getMailSender(mailProperties: MailProperties): JavaMailSender {
        return JavaMailSenderImpl().apply {
            host = mailProperties.host
            port = mailProperties.port
            username = mailProperties.username
            password = mailProperties.password
            javaMailProperties.setProperty(MAIL_SMTP_PROTOCOL, mailProperties.protocol)
            javaMailProperties.setProperty(MAIL_SMTP_AUTH, mailProperties.auth)
            javaMailProperties.setProperty(MAIL_SMTP_SSL, mailProperties.sslEnable)
            javaMailProperties.setProperty(MAIL_SMTP_DEBUG, mailProperties.smtpDebug.toString())
        }
    }
}