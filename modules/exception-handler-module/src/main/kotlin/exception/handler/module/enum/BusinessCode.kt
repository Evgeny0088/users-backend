package exception.handler.module.enum

enum class BusinessCode(
    private val businessCode: String
) {

    ERR_DEFAULT("ERR-0"),
    ERR_400("ERR-400"),
    ERR_401("ERR-401"),
    ERR_EMAIL_401("ERR-EMAIL-401"),
    ERR_TOKEN_401("ERR-TOKEN-401"),
    ERR_402("ERR-402"),
    ERR_403("ERR-403"),
    ERR_404("ERR-404"),
    ERR_409("ERR-409"),
    ERR_410("ERR-410");

    fun getBusinessCode(): String = this.businessCode

}
