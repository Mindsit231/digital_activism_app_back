package mindsit.digitalactivismapp.service.authentication;

import jakarta.mail.MessagingException;
import mindsit.digitalactivismapp.mapper.MemberMapper;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorLists;
import mindsit.digitalactivismapp.modelDTO.authentication.login.LoginRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.PasswordRecoveryContainer;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.RecoverPasswordRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.RecoverPasswordResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordReset.ResetPasswordRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordReset.ResetPasswordResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.register.RegisterResponse;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.*;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.service.JWTService;
import mindsit.digitalactivismapp.service.misc.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static mindsit.digitalactivismapp.config.SecurityConfig.PASSWORD_ROUNDS;
import static mindsit.digitalactivismapp.custom.Functions.generateRandomNumber;
import static mindsit.digitalactivismapp.custom.Functions.getToken;
import static mindsit.digitalactivismapp.service.authentication.TempDataService.getVerificationCodeKey;

@Service
public class AuthenticationService {

    public final static String USERNAME_ERROR_LIST = "Username";
    public final static String EMAIL_ERROR_LIST = "Email";
    public final static String PASSWORD_ERROR_LIST = "Password";
    public final static String TOKEN_ERROR_LIST = "Token";

    private final static int MIN_PASSWORD_LENGTH = 12;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final MemberRepository memberRepository;
    private final AuthenticationManager authManager;
    private final EmailService emailService;
    private final JWTService jwtService;
    private final ApplicationContext context;
    private final MemberMapper memberMapper;
    private final TempDataService tempDataService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(PASSWORD_ROUNDS);

    @Autowired
    public AuthenticationService(MemberRepository memberRepository,
                                 AuthenticationManager authManager, EmailService emailService,
                                 JWTService jwtService,
                                 ApplicationContext context,
                                 MemberMapper memberMapper, TempDataService tempDataService) {
        this.memberRepository = memberRepository;
        this.authManager = authManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.context = context;
        this.memberMapper = memberMapper;
        this.tempDataService = tempDataService;
    }

    private static ResponseEntity<MemberDTO> getUnauthorizedResponse(LoginRequest loginRequest) {
        logger.info("Failed login attempt for: {}", loginRequest.email());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public static void hashPassword(BCryptPasswordEncoder encoder, ErrorLists errorLists, Member member, String newPassword) {
        ErrorList passwordErrorList = new ErrorList(PASSWORD_ERROR_LIST);
        errorLists.add(passwordErrorList);

        if (newPassword.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{" + MIN_PASSWORD_LENGTH + ",}$")) {
            member.setPassword(encoder.encode(newPassword));
        } else {
            passwordErrorList.getErrors().add("Password cannot be empty, must contain at least " +
                    MIN_PASSWORD_LENGTH +
                    " characters, 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.");
        }
    }

    public Optional<Member> findMemberByToken(String token) {
        return Optional.ofNullable(memberRepository.findByToken(token));
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public ResponseEntity<MemberDTO> verifyLogin(LoginRequest loginRequest) {
        try {
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            if (authentication.isAuthenticated()) {
                Member foundMember = memberRepository.findByEmail(loginRequest.email());
                // force token update on every login

//            UserDetails userDetails = context.getBean(MemberDetailsService.class).loadUserByUsername(loginRequest.email());
//
//            if (foundMember.getToken() == null ||
//                    foundMember.getToken().isEmpty() ||
//                    !jwtService.validateToken(foundMember.getToken(), userDetails)) {
//                updateMemberToken(foundMember);
//            }
                logger.info("User {} logged in.", loginRequest.email());
                updateMemberToken(foundMember);
                return ResponseEntity.ok(memberMapper.memberToMemberDTO(foundMember));
            } else {
                return getUnauthorizedResponse(loginRequest);
            }
        } catch (AuthenticationException e) {
            return getUnauthorizedResponse(loginRequest);
        }

    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        Member member = memberMapper.memberToRegisterMapper(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse();

        checkEmail(registerResponse.getErrorLists(), member);
        checkUsername(registerResponse.getErrorLists(), member);
        hashPassword(encoder, registerResponse.getErrorLists(), member, registerRequest.password());

        if (registerResponse.hasNoErrors()) {
            registerResponse.setToken(updateMemberToken(member));
            memberRepository.save(member);
        }

        return registerResponse;
    }

    private String updateMemberToken(Member member) {
        logger.info("Generating new token for: {}", member.getEmail());
        String token = jwtService.generateToken(member);
        member.setToken(token);
        memberRepository.updateTokenByEmail(member.getEmail(), member.getToken());
        return token;
    }

    public void checkEmail(ErrorLists errorLists, Member member) {
        ErrorList emailErrorList = new ErrorList(EMAIL_ERROR_LIST);
        errorLists.add(emailErrorList);

        if (member.getEmail() == null || member.getEmail().isEmpty()) {
            emailErrorList.getErrors().add("Email cannot be empty.");
            return;
        }

        if (member.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            member.setEmail(member.getEmail().toLowerCase());
        } else {
            emailErrorList.getErrors().add("Invalid email.");
            return;
        }

        if (findMemberByEmail(member.getEmail()) != null) {
            emailErrorList.getErrors().add("Email already in use.");
        }
    }

    public void checkUsername(ErrorLists errorLists, Member member) {
        ErrorList usernameErrorList = new ErrorList(USERNAME_ERROR_LIST);
        errorLists.add(usernameErrorList);

        if (member.getUsername() == null || member.getUsername().isEmpty()) {
            usernameErrorList.getErrors().add("Username cannot be empty.");
            return;
        }

        if (member.getUsername().matches("^[a-zA-Z0-9]*$")) {
            member.setUsername(member.getUsername().toLowerCase());
        } else {
            usernameErrorList.getErrors().add("Username must contain only letters and numbers.");
            return;
        }


        if (member.getUsername().length() < 3) {
            usernameErrorList.getErrors().add("Username must be at least 3 characters long.");
        }
    }

    @Transactional
    public ResponseEntity<MemberDTO> loginByToken(String authHeader) {
        return getToken(authHeader).map(token ->
                        findMemberByToken(token).map(member -> ResponseEntity.ok(memberMapper.memberToMemberDTO(member)))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public ResponseEntity<SendEmailVerificationResponse> sendEmailVerification(SendEmailVerificationRequest sendEmailVerificationRequest) {
        int verificationCode = generateRandomNumber(5);
        logger.info("Verification code for {}: {}", sendEmailVerificationRequest.email(), verificationCode);
        tempDataService.putValue(getVerificationCodeKey(sendEmailVerificationRequest.email()), String.valueOf(verificationCode));

        EmailVerificationContainer emailVerificationContainer = new EmailVerificationContainer(
                sendEmailVerificationRequest.email(),
                "Verification Email",
                null,
                true,
                "email-verification-template",
                verificationCode);

        SendEmailVerificationResponse sendEmailVerificationResponse = new SendEmailVerificationResponse();

        try {
            emailService.sendVerificationEmail(emailVerificationContainer);
            return ResponseEntity.ok(sendEmailVerificationResponse);
        } catch (MessagingException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            sendEmailVerificationResponse.getErrors().add("Failed to send verification email.");
            return ResponseEntity.ok(sendEmailVerificationResponse);
        }
    }

    @Transactional
    public ResponseEntity<VerifyEmailResponse> verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
        Member member = findMemberByEmail(verifyEmailRequest.email());

        if (member == null) {
            verifyEmailResponse.getErrors().add("User with this email not found.");
        }

        if (tempDataService.getValue(getVerificationCodeKey(verifyEmailRequest.email())).toString().equals(verifyEmailRequest.verificationCode())) {
            tempDataService.removeValue(getVerificationCodeKey(verifyEmailRequest.email()));

            memberRepository.updateEmailVerifiedByEmail(verifyEmailRequest.email());
            verifyEmailResponse.setSuccess(true);
        } else {
            verifyEmailResponse.getErrors().add("Verification Code is Invalid.");
        }
        return ResponseEntity.ok(verifyEmailResponse);
    }

    @Transactional
    public ResponseEntity<RecoverPasswordResponse> recoverPassword(RecoverPasswordRequest recoverPasswordRequest) {
        RecoverPasswordResponse recoverPasswordResponse = new RecoverPasswordResponse();
        Member member = findMemberByEmail(recoverPasswordRequest.email());

        if (member == null) {
            recoverPasswordResponse.getErrors().add("User with this email not found.");
        } else {
            String token = updateMemberToken(member);
            PasswordRecoveryContainer passwordRecoveryContainer = new PasswordRecoveryContainer(
                    member.getEmail(),
                    "Password Recovery",
                    null,
                    true,
                    "password-recovery-template",
                    token,
                    recoverPasswordRequest.pagePath());
            try {
                emailService.sendPasswordRecoveryEmail(passwordRecoveryContainer);
                recoverPasswordResponse.setSuccess(true);
            } catch (MessagingException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                recoverPasswordResponse.getErrors().add("Failed to send password recovery email.");
            }
        }

        return ResponseEntity.ok(recoverPasswordResponse);
    }

    @Transactional
    public ResponseEntity<ResetPasswordResponse> resetPassword(ResetPasswordRequest resetPasswordRequest, String authHeader) {
        ResetPasswordResponse resetPasswordResponse = new ResetPasswordResponse();
        resetPasswordSub(resetPasswordResponse.getErrorLists(), resetPasswordRequest.newPassword(), authHeader);
        if (resetPasswordResponse.getErrorLists().hasNoErrors()) {
            resetPasswordResponse.setSuccess(true);
        }

        return ResponseEntity.ok(resetPasswordResponse);
    }

    @Transactional
    public void resetPasswordSub(ErrorLists errorLists, String newPassword, String authHeader) {
        ErrorList passwordErrorList = new ErrorList(PASSWORD_ERROR_LIST);
        ErrorList tokenErrorList = new ErrorList(TOKEN_ERROR_LIST);

        errorLists.add(tokenErrorList);
        errorLists.add(passwordErrorList);


        if (newPassword != null && !newPassword.isEmpty()) {
            String token = getToken(authHeader).orElse("");
            Optional<Member> memberOpt = findMemberByToken(token);

            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                if (encoder.matches(newPassword, member.getPassword())) {
                    passwordErrorList.getErrors().add("New password cannot be the same as the old password.");
                } else {
                    hashPassword(encoder, errorLists, member, newPassword);
                    logger.info("Password reset for: {}", member.getEmail());
                    this.memberRepository.updatePasswordByToken(token, member.getPassword());
                }

            } else {
                tokenErrorList.getErrors().add("Invalid token.");
            }
        } else {
            passwordErrorList.getErrors().add("New password cannot be empty.");
        }
    }

    public ResponseEntity<Boolean> verifyToken(String authHeader) {
        return getToken(authHeader).map(token ->
                        findMemberByToken(token).map(member -> ResponseEntity.ok(true))
                                .orElseGet(() -> ResponseEntity.ok(false)))
                .orElseGet(() -> ResponseEntity.ok(false));
    }

    public ResponseEntity<Boolean> checkOldPassword(String oldPassword, String authHeader) {
        return getToken(authHeader)
                .map(token -> findMemberByToken(token)
                        .map(member -> {
                            if (encoder.matches(oldPassword, member.getPassword())) {
                                System.out.println("Old password matches current password.");
                                return ResponseEntity.ok(true);
                            } else {
                                System.out.println("Old password does not match current password.");
                                return ResponseEntity.ok(false);
                            }
                        })
                        .orElseGet(() -> ResponseEntity.ok(false)))
                .orElseGet(() -> ResponseEntity.ok(false));
    }
}
