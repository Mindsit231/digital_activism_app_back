package mindsit.digitalactivismapp.service.authentication;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.LoginRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.MemberDTOOptional;
import mindsit.digitalactivismapp.modelDTO.authentication.RegisterRequest;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.service.JWTService;
import mindsit.digitalactivismapp.service.member.MemberDTOMapper;
import mindsit.digitalactivismapp.service.member.MemberDetailsService;
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

import static mindsit.digitalactivismapp.config.Functions.getToken;
import static mindsit.digitalactivismapp.config.SecurityConfig.PASSWORD_ROUNDS;

@Service
public class AuthenticationService {

    private final static int MIN_PASSWORD_LENGTH = 12;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final ApplicationContext context;
    private final MemberDTOMapper memberDTOMapper;
    private final RegisterMapper registerMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(PASSWORD_ROUNDS);

    @Autowired
    public AuthenticationService(MemberRepository memberRepository,
                         AuthenticationManager authManager,
                         JWTService jwtService,
                         ApplicationContext context, MemberDTOMapper memberDTOMapper, RegisterMapper registerMapper) {
        this.memberRepository = memberRepository;
        this.authManager = authManager;
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
            UserDetails userDetails = context.getBean(MemberDetailsService.class).loadUserByUsername(loginRequest.email());

            if (foundMember.getToken() == null ||
                    foundMember.getToken().isEmpty() ||
                    !jwtService.validateToken(foundMember.getToken(), userDetails)) {
                updateMemberToken(foundMember);
            }
            return ResponseEntity.ok(memberDTOMapper.apply(foundMember));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public MemberDTOOptional register(RegisterRequest registerRequest) {
        Member member = registerMapper.apply(registerRequest);
        MemberDTOOptional memberDTOOptional = new MemberDTOOptional();

        checkEmail(memberDTOOptional, member);
        checkUsername(memberDTOOptional, member);
        hashPassword(memberDTOOptional, member);

        if(memberDTOOptional.getErrors().isEmpty()) {
            updateMemberToken(member);
            memberRepository.save(member);
            memberDTOOptional.setMemberDTO(memberDTOMapper.apply(member));
        }

        return memberDTOOptional;
    }

    private void updateMemberToken(Member member) {
        System.out.println("Token is null or invalid, generating new token for: " + member.getEmail());
        member.setToken(jwtService.generateToken(member));
        memberRepository.updateTokenByEmail(member.getEmail(), member.getToken());
    }

    private void checkEmail(MemberDTOOptional memberDTOOptional, Member member) {
        if(member.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            member.setEmail(member.getEmail().toLowerCase());
        } else {
            memberDTOOptional.getErrors().add("Invalid email.");
            return;
        }

        if(findMemberByEmail(member.getEmail()) != null) {
            memberDTOOptional.getErrors().add("Email already in use.");
        }
    }

    private void checkUsername(MemberDTOOptional memberDTOOptional, Member member) {
        if(member.getUsername().matches("^[a-zA-Z0-9]*$")) {
            member.setUsername(member.getUsername().toLowerCase());
        } else {
            memberDTOOptional.getErrors().add("Username must contain only letters and numbers.");
            return;
        }


        if(member.getUsername().length() < 3) {
            memberDTOOptional.getErrors().add("Username must be at least 3 characters long.");
        }
    }

    public void hashPassword(MemberDTOOptional memberDTOOptional, Member member) {
        if (member.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{" + MIN_PASSWORD_LENGTH + ",}$")) {
            member.setPassword(encoder.encode(member.getPassword()));
        } else {
            memberDTOOptional.getErrors().add("Password cannot be empty, must contain at least " +
                    MIN_PASSWORD_LENGTH +
                    " characters, 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character.");
        }
    }

    public ResponseEntity<MemberDTO> loginByToken(String authHeader) {
        return getToken(authHeader).map(token ->
                        findMemberByToken(token).map(memberDto -> ResponseEntity.ok(memberDTOMapper.apply(memberDto)))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public ResponseEntity<HttpStatus> verifyEmail(String authHeader) {
        return getToken(authHeader).map(token ->
                        findMemberByToken(token).map(memberDto -> ResponseEntity.ok(HttpStatus.ACCEPTED))
                                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    public ResponseEntity<HttpStatus> sendVerificationEmail(String authHeader) {
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}
