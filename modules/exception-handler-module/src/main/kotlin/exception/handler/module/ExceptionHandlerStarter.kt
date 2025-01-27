package exception.handler.module

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import service.config.module.ServiceConfigStarter

@ComponentScan
@Import(ServiceConfigStarter::class)
class ExceptionHandlerStarter