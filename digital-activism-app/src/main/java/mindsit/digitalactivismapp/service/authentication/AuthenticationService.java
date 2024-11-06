package mindsit.digitalactivismapp.service.authentication;

import jakarta.mail.MessagingException;
import mindsit.digitalactivismapp.mapper.member.RegisterMapper;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.authentication.*;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.service.JWTService;
import mindsit.digitalactivismapp.mapper.member.MemberDTOMapper;
import mindsit.digitalactivismapp.service.member.MemberDetailsService;
import mindsit.digitalactivismapp.service.misc.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.generateRandomNumber;
import static mindsit.digitalactivismapp.custom.Functions.getToken;
import static mindsit.digitalactivismapp.config.SecurityConfig.PASSWORD_ROUNDS;

@Service
public class AuthenticationService {

    private final static int MIN_PASSWORD_LENGTH = 12;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authManager;
    private final EmailService emailService;
    private final JWTService jwtService;
    private final ApplicationContext context;
    private final MemberDTOMapper memberDTOMapper;
    private final RegisterMapper registerMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(PASSWORD_ROUNDS);

    @Autowired
    public AuthenticationService(MemberRepository memberRepository,
                                 AuthenticationManager authManager, EmailService emailService,
                                 JWTService jwtService,
                                 ApplicationContext context, MemberDTOMapper memberDTOMapper, RegisterMapper registerMapper) {
        this.memberRepository = memberRepository;
        this.authManager = authManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.context = context;
        this.memberDTOMapper = memberDTOMapper;
        this.registerMapper = registerMapper;
    }

    public Optional<Member> findMemberByToken(String token) {
        return Optional.ofNullable(memberRepository.findByToken(token));
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public ResponseEntity<MemberDTO> verifyLogin(LoginRequest loginRequest) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        if (authentication.isAuthenticated()) {
            Member foundMember = memberRepository.findByEmail(loginRequest.email());
            // force token update on every login
            updateMemberToken(foundMember);
//            UserDetails userDetails = context.getBean(MemberDetailsService.class).loadUserByUsername(loginRequest.email());
//
//            if (foundMember.getToken() == null ||
//                    foundMember.getToken().isEmpty() ||
//                    !jwtService.validateToken(foundMember.getToken(), userDetails)) {
//                updateMemberToken(foundMember);
//            }
            return ResponseEntity.ok(memberDTOMapper.apply(foundMember));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        Member member = registerMapper.apply(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse();

        checkEmail(registerResponse, member);
        checkUsername(registerResponse, member);
        hashPassword(registerResponse, member);

        if(registerResponse.hasNoErrors()) {
            registerResponse.setToken(updateMemberToken(member));
            memberRepository.save(member);
        }

        return registerResponse;
    }

    private String updateMemberToken(Member member) {
        System.out.println("Token is null or invalid, generating new token for: " + member.getEmail());
        String token = jwtService.generateToken(member);
        member.setToken(token);
        memberRepository.updateTokenByEmail(member.getEmail(), member.getToken());
        return token;
    }

    private void checkEmail(RegisterResponse registerResponse, Member member) {
        ErrorList errorList = new ErrorList("Email");
        registerResponse.getErrorLists().add(errorList);

        if(member.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            member.setEmail(member.getEmail().toLowerCase());
        } else {
            errorList.getErrors().add("Invalid email.");
            return;
        }

        if(findMemberByEmail(member.getEmail()) != null) {
            errorList.getErrors().add("Email already in use.");
        }
    }

    private void checkUsername(RegisterResponse registerResponse, Member member) {
        ErrorList errorList = new ErrorList("Username");
        registerResponse.getErrorLists().add(errorList);

        if(member.getUsername().matches("^[a-zA-Z0-9]*$")) {
            member.setUsername(member.getUsername().toLowerCase());
        } else {
            errorList.getErrors().add("Username must contain only letters and numbers.");
            return;
        }


        if(member.getUsername().length() < 3) {
            errorList.getErrors().add("Username must be at least 3 characters long.");
        }
    }

    public void hashPassword(RegisterResponse registerResponse, Member member) {
        ErrorList errorList = new ErrorList("Password");
        registerResponse.getErrorLists().add(errorList);

        if (member.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{" + MIN_PASSWORD_LENGTH + ",}$")) {
            member.setPassword(encoder.encode(member.getPassword()));
        } else {
            errorList.getErrors().add("Password cannot be empty, must contain at least " +
                    MIN_PASSWORD_LENGTH +
                    " characters, 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.");
        }
    }

    @Transactional
    public ResponseEntity<MemberDTO> loginByToken(String authHeader) {
        return getToken(authHeader).map(token ->
                        findMemberByToken(token).map(member -> ResponseEntity.ok(memberDTOMapper.apply(member)))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Transactional
    public ResponseEntity<VerifyEmailResponse> verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse();
        Member member = findMemberByEmail(verifyEmailRequest.email());

        if (member == null) {
            verifyEmailResponse.getErrors().add("User with this email not found.");
        }

        if (encoder.matches(verifyEmailRequest.verificationCode(), verifyEmailRequest.verificationCodeHash())) {
            memberRepository.updateEmailVerifiedByEmail(verifyEmailRequest.email());
            verifyEmailResponse.setSuccess(true);
        } else {
            verifyEmailResponse.getErrors().add("Invalid verification code.");
        }
        return ResponseEntity.ok(verifyEmailResponse);
    }

    public ResponseEntity<SendEmailVerificationResponse> sendEmailVerification(SendEmailVerificationRequest sendEmailVerificationRequest) {
        int verificationCode = generateRandomNumber(5);
        System.out.println("Verification code for " + sendEmailVerificationRequest.email() +  ": " + verificationCode);
        String verificationCodeHash = encoder.encode(String.valueOf(verificationCode));
        EmailVerificationContainer emailVerificationContainer = new EmailVerificationContainer(
                sendEmailVerificationRequest.email(),
                "Verification Email",
                "Here is your verification code: " + verificationCode,
                true,
                "email-verification-template",
                verificationCode);

        SendEmailVerificationResponse sendEmailVerificationResponse = new SendEmailVerificationResponse();

        try {
            emailService.sendVerificationEmail(emailVerificationContainer);
            sendEmailVerificationResponse.setVerificationCodeHash(verificationCodeHash);
            return ResponseEntity.ok(sendEmailVerificationResponse);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            sendEmailVerificationResponse.getErrors().add("Failed to send verification email.");
            return ResponseEntity.ok(sendEmailVerificationResponse);
        }
    }

}
