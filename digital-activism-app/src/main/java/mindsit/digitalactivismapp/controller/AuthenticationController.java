package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.login.LoginRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.RecoverPasswordRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.RecoverPasswordResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordReset.ResetPasswordRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordReset.ResetPasswordResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.SendEmailVerificationRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.SendEmailVerificationResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.VerifyEmailRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.VerifyEmailResponse;
import mindsit.digitalactivismapp.service.authentication.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    public final static String AUTHORIZATION_HEADER = "Authorization";
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

    @PostMapping("authenticated/verify-token")
    public ResponseEntity<Boolean> verifyToken(@RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return authenticationService.verifyToken(authHeader);
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

    @PostMapping("/public/recover-password")
    public ResponseEntity<RecoverPasswordResponse> recoverPassword(@RequestBody RecoverPasswordRequest recoverPasswordRequest) {
        return authenticationService.recoverPassword(recoverPasswordRequest);
    }

    @PostMapping("/authenticated/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                               @RequestBody ResetPasswordRequest recoverPasswordRequest) {
        return authenticationService.resetPassword(recoverPasswordRequest, authHeader);
    }

    @PostMapping("/authenticated/check-old-password")
    public ResponseEntity<Boolean> checkOldPassword(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                    @RequestBody String oldPassword) {
        return authenticationService.checkOldPassword(oldPassword, authHeader);
    }
}
