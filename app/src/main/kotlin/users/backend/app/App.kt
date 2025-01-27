package users.backend.app

import auth.module.AuthStarter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(AuthStarter::class)
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
