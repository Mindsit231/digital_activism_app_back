package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.*;
import mindsit.digitalactivismapp.service.authentication.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String EMAIL_PARAM = "Email";
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/public/login")
    public ResponseEntity<MemberDTO> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.verifyLogin(loginRequest);
    }

    @PostMapping("/public/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/authenticated/login-by-token")
    public ResponseEntity<MemberDTO> loginByToken(@RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return authenticationService.loginByToken(authHeader);
    }

    @PostMapping("/authenticated/send-verification-email")
    public ResponseEntity<SendEmailVerificationResponse> sendEmailVerification(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                                               @RequestBody SendEmailVerificationRequest sendEmailVerificationRequest) {
        return authenticationService.sendEmailVerification(sendEmailVerificationRequest);
    }

    @PostMapping("/authenticated/verify-email")
    public ResponseEntity<VerifyEmailResponse> verifyEmail(@RequestBody VerifyEmailRequest verifyEmailRequest) {
        return authenticationService.verifyEmail(verifyEmailRequest);
    }
}
