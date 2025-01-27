package auth.module.mapper

import auth.module.dto.LoginRequest
import auth.module.dto.Token
import org.keycloak.representations.AccessTokenResponse
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
abstract class TokenMapper: GenericMapper<LoginRequest, AccessTokenResponse, Token>