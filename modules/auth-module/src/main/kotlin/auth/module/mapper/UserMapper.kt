package auth.module.mapper

import auth.module.dto.SignUpRequest
import auth.module.dto.UserResponse
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class UserMapper: GenericAuthMapper<SignUpRequest, UserRepresentation, UserResponse> {

    @AfterMapping
    fun setupCredentials(@MappingTarget userRepresentation: UserRepresentation, req: SignUpRequest) {
        userRepresentation.also {
            it.credentials = listOf(createPasswordCredentials(req.password))
        }
    }

    private fun createPasswordCredentials(password: String): CredentialRepresentation {
        val passwordCredentials = CredentialRepresentation()
        passwordCredentials.isTemporary = false
        passwordCredentials.type = CredentialRepresentation.PASSWORD
        passwordCredentials.value = password
        return passwordCredentials
    }
}
