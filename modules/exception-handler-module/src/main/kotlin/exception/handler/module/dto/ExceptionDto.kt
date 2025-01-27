package exception.handler.module.dto

import exception.handler.module.enum.BusinessCode

data class ExceptionDto (
    val httpCode: Int,
    val businessCode: String? = BusinessCode.ERR_DEFAULT.getBusinessCode(),
    val message: String? = null,
    val details: Any? = null
)