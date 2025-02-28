package exception.handler.module.mapper

import exception.handler.module.dto.ExceptionDto
import exception.handler.module.enum.BusinessCode
import exception.handler.module.exception.CustomException
import exception.handler.module.exception.ValidationException
import jakarta.ws.rs.ClientErrorException
import jakarta.ws.rs.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException

@Component
class ExceptionMapper {

    fun <T: Throwable>mapFromException(exception: T): ExceptionDto {
        return ExceptionDto(
            httpCode = 500,
            message = exception.localizedMessage ?: "Internal error."
        )
    }

    fun mapFromWebArgumentsException(exception: ValidationException): ExceptionDto {
        return mapValidationExceptionToCustomDto(exception)
    }

    fun mapFromCustomException(exception: CustomException): ExceptionDto {
        return mapCustomExceptionToCustomDto(exception)
    }

    fun mapFromNoFoundException(exception: NotFoundException): ExceptionDto {
        return ExceptionDto(
            message = exception.localizedMessage + ". Keycloak resource not found.",
            httpCode = exception.response.status,
            businessCode = BusinessCode.ERR_404.getBusinessCode()
        )
    }

    fun mapFromClientErrorException(exception: ClientErrorException): ExceptionDto {
        return ExceptionDto(
            message = exception.localizedMessage ?: "Keycloak client error occurred.",
            httpCode = exception.response.status,
            businessCode = BusinessCode.ERR_409.getBusinessCode()
        )
    }

    private fun mapValidationExceptionToCustomDto(exception: ValidationException): ExceptionDto {
        return ExceptionDto(
            message = exception.errorMessage,
            httpCode = HttpStatus.BAD_REQUEST.value(),
            businessCode = exception.businessCode?.getBusinessCode(),
            details = exception.details
        )
    }

    private fun mapCustomExceptionToCustomDto(exception: CustomException): ExceptionDto {
        return ExceptionDto(
            httpCode = exception.httpStatus,
            businessCode = exception.businessCode?.getBusinessCode(),
            message = exception.errorMessage,
            details = exception.details
        )
    }

    fun mapFromWebClientResponseException(exception: WebClientResponseException): ExceptionDto {
        return ExceptionDto(
            message = exception.message,
            httpCode = exception.statusCode.value()
        )
    }
}
