package auth.module

import email.service.module.EmailServiceConfigStarter
import exception.handler.module.ExceptionHandlerStarter
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import service.config.module.ServiceConfigStarter
import users.module.UsersStarter

@ComponentScan
@Import(
    ExceptionHandlerStarter::class,
    ServiceConfigStarter::class,
    UsersStarter::class,
    EmailServiceConfigStarter::class
)
class AuthStarter
