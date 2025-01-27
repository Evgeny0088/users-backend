package auth.module.dto

class SignUpRequest {
    var username: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var password: String = ""
    var email: String = ""
    var isEnabled: Boolean = false
    var role: String? = null
}
