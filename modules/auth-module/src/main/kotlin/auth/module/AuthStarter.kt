package auth.module

import exception.handler.module.ExceptionHandlerStarter
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import service.config.module.ServiceConfigStarter

@ComponentScan
@Import(
    ExceptionHandlerStarter::class,
    ServiceConfigStarter::class
)
class AuthStarter