//package auth.module.controller
//
//import auth.module.dto.LoginRequest
//import auth.module.dto.SignUpRequest
//import auth.module.dto.Token
//import auth.module.dto.UserResponse
//import auth.module.service.KeycloakService
//import auth.module.validator.LoginRequestValidator
//import auth.module.validator.SignRequestValidator
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import javax.validation.Valid
//
//@RestController
//@RequestMapping("/api/v1/auth")
//class AuthController(
//    private val keycloakService: KeycloakService,
//    private val signValidator: SignRequestValidator,
//    private val loginValidator: LoginRequestValidator
//) {
//
//    @PostMapping("/sign-up")
//    suspend fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse> {
//        signValidator.validate(signUpRequest)
//        return ResponseEntity.ok(keycloakService.signUp(signUpRequest))
//    }
//
//    @PostMapping("/login")
//    suspend fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Token> {
//        return ResponseEntity.ok(keycloakService.login(loginRequest))
//    }
//}