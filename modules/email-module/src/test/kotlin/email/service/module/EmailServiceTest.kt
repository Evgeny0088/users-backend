package email.service.module

import email.service.module.dto.EmailDto
import email.service.module.service.EmailService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.time.LocalDate

class EmailServiceTest {
    private val emailService = mock(EmailService::class.java)

    @Test
    fun serviceTest() {
        runBlocking {
            emailService.sendEmail(EmailDto("user", "test@gmail.com", "link.com", LocalDate.now()))
        }
    }
}