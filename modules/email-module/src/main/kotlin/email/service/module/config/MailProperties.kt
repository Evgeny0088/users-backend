package email.service.module.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.mail")
data class MailProperties(
    var host: String = "",
    val port: Int = 0,
    val protocol: String = "",
    val auth: String = "",
    val sslEnable: String = "",
    val username: String = "",
    val password: String = "",
    val sendTo: String = "",
    var smtpDebug: Boolean = false
)
