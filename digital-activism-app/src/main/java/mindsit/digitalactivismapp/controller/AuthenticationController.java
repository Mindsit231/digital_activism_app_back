package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.LoginRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.MemberDTOOptional;
import mindsit.digitalactivismapp.modelDTO.authentication.RegisterRequest;
import mindsit.digitalactivismapp.service.authentication.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/public/login")
    public ResponseEntity<MemberDTO> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.verifyLogin(loginRequest);
    }

    @PostMapping("/public/register")
    public ResponseEntity<MemberDTOOptional> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @GetMapping("/authenticated/login-by-token")
    public ResponseEntity<MemberDTO> loginByToken(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.loginByToken(authHeader);
    }

    @GetMapping("/authenticated/verify-email")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestHeader("Authorization") String authHeader) {
        return authenticationService.verifyEmail(authHeader);
    }

    @GetMapping("/authenticated/send-verification-email")
    public ResponseEntity<HttpStatus> sendVerificationEmail(@RequestHeader("Authorization") String authHeader, @RequestParam String email) {
        return authenticationService.sendVerificationEmail(authHeader);
    }
}
