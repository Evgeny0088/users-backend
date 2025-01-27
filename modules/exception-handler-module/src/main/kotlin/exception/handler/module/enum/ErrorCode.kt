package exception.handler.module.enum

enum class ErrorCode(
    private var httpCode: Int,
    private val businessCode: BusinessCode? = BusinessCode.ERR_DEFAULT,
) {
    USER_NOT_REGISTERED(400, BusinessCode.ERR_402),
    USER_LOGIN_FAILED(401, BusinessCode.ERR_401),
    USER_IS_FORBIDDEN(403, BusinessCode.ERR_403),
    INPUT_BAD_REQUEST(400, BusinessCode.ERR_400),
    USER_IS_NOT_DELETED(400, BusinessCode.ERR_410),
    USER_ALREADY_REGISTERED(409, BusinessCode.ERR_409);

    fun setHttpCode(httpCode: Int): ErrorCode {
        this.httpCode = httpCode
        return this
    }

    fun getBusinessCode(): String =
        this.businessCode?.getBusinessCode()
            ?: BusinessCode.ERR_DEFAULT.getBusinessCode()
}