package exception.handler.module.exception

import exception.handler.module.enum.ErrorCode
import org.springframework.http.HttpStatus

class ValidationException: CustomException {

    constructor(
        errorMessage: String,
        businessCode: ErrorCode,
        details: Any
    ): super(errorMessage) {
        this.errorMessage = errorMessage
        this.httpStatus = HttpStatus.BAD_REQUEST.value()
        this.businessCode = businessCode
        this.businessCode?.setHttpCode(httpStatus)
        this.details = details
    }
}
