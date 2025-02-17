package exception.handler.module

import exception.handler.module.config.ErrorsMessageResolver
import exception.handler.module.validator.ValidationHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.context.annotation.*
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import service.config.module.Constants.BEAN_BUSINESS_ERRORS
import service.config.module.Constants.BEAN_VALIDATION_ERRORS
import service.config.module.ServiceConfigStarter

@ComponentScan
@Configuration(proxyBeanMethods = false)
@Import(ServiceConfigStarter::class)
class ExceptionHandlerStarter {

    @Bean(BEAN_VALIDATION_ERRORS)
    fun validationErrorsMessageSource(): MessageSource {
        return messageSourceCreator("validation/ValidationMessages")
    }

    @Bean(BEAN_BUSINESS_ERRORS)
    fun businessErrorsMessageSource(): MessageSource {
        return messageSourceCreator("messages/messages")
    }

    @Bean
    @Primary
    fun springValidator(
        @Qualifier(BEAN_VALIDATION_ERRORS) messageSource: MessageSource): Validator {
        val bean = LocalValidatorFactoryBean()
        bean.setValidationMessageSource(messageSource)
        return bean
    }

    @Bean
    fun validationHandler(
        validator: Validator,
        customMessageResolver: ErrorsMessageResolver): ValidationHandler<Validator> {
        return ValidationHandler(validator, customMessageResolver)
    }

    private fun messageSourceCreator(bundleName: String): MessageSource {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasename(bundleName)
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}