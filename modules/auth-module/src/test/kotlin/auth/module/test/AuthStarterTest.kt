package auth.module.test

import auth.module.dto.SignUpRequest
import auth.module.mapper.TokenMapperImpl
import auth.module.mapper.UserMapperImpl
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.CredentialRepresentation

class AuthStarterTest {

    private var userMapper: UserMapperImpl = UserMapperImpl()
    private val tokenMapper: TokenMapperImpl = TokenMapperImpl()

    @Test
    fun `user mapper test`() {
        val signUpRequest = SignUpRequest().apply {
            this.username = "test"
            this.password = "password"
            this.firstName = "user-1"
            this.lastName = "user-1"
            this.email = "user@test.com"
        }

        val userRepresentation = userMapper.toRepresentation(signUpRequest)

        userRepresentation.username shouldBe signUpRequest.username
        userRepresentation.firstName shouldBe signUpRequest.firstName
        userRepresentation.email shouldBe signUpRequest.email
        userRepresentation.isEnabled shouldBe signUpRequest.isEnabled

        userRepresentation.credentials[0].type shouldBe CredentialRepresentation.PASSWORD
        userRepresentation.credentials[0].value shouldBe signUpRequest.password
        userRepresentation.credentials[0].isTemporary shouldBe false
    }

    @Test
    fun `token mapper test`() {
        val accessToken: AccessTokenResponse = AccessTokenResponse().also {
            it.token = "access token"
            it.refreshToken = "refreshToken"
            it.idToken = "id token"
            it.expiresIn = 1800
            it.refreshExpiresIn = 2800
            it.sessionState = "session state"
        }

        val tokenResponse = tokenMapper.toDto(accessToken)

        tokenResponse.token shouldBe accessToken.token
        tokenResponse.refreshToken shouldBe accessToken.refreshToken
        tokenResponse.expiresIn shouldBe  accessToken.expiresIn
        tokenResponse.refreshExpiresIn shouldBe accessToken.refreshExpiresIn
        tokenResponse.sessionState shouldBe accessToken.sessionState

        tokenResponse.tokenType shouldBe null
        tokenResponse.scope shouldBe null
    }
}