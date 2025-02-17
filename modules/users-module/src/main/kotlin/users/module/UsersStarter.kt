package users.module

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import service.config.module.ServiceConfigStarter

@ComponentScan
@EnableR2dbcRepositories
@Import(ServiceConfigStarter::class)
class UsersStarter