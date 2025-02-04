package exception.handler.module.dto

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import exception.handler.module.enum.BusinessCode

@JsonPropertyOrder
data class ExceptionDto (
    val message: String? = null,
    val httpCode: Int,
    val businessCode: String? = BusinessCode.ERR_DEFAULT.getBusinessCode(),
    val details: Any? = null
)