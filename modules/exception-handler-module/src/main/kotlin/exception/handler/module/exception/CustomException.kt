package exception.handler.module.exception

import exception.handler.module.enum.ErrorCode

open class CustomException: RuntimeException {

    var errorMessage: String
    var httpStatus: Int = 0
    var businessCode: ErrorCode? = null
    var details: Any? = null

    constructor(errorMessage: String) : super(errorMessage) {
        this.errorMessage = errorMessage
    }

    constructor(errorMessage: String, httpStatus: Int) : super(errorMessage) {
        this.errorMessage = errorMessage
        this.httpStatus = httpStatus
    }

    constructor(
        errorMessage: String,
        httpStatus: Int,
        businessCode: ErrorCode
    ): super(errorMessage) {
        this.errorMessage = errorMessage
        this.httpStatus = httpStatus
        this.businessCode = businessCode
        this.businessCode?.setHttpCode(httpStatus)
    }
}