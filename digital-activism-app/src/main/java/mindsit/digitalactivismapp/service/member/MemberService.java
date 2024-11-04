package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PasswordByEmail;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByOldToken;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.LoginRequest;
import mindsit.digitalactivismapp.modelDTO.authentication.MemberDTOOptional;
import mindsit.digitalactivismapp.modelDTO.authentication.RegisterRequest;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.JWTService;
import mindsit.digitalactivismapp.service.authentication.RegisterMapper;
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

import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.config.Functions.getToken;
import static mindsit.digitalactivismapp.config.SecurityConfig.PASSWORD_ROUNDS;

@Service
public class MemberService extends EntityService<Member, MemberRepository> {

//    private final static int MIN_PASSWORD_LENGTH = 12;
//    private final AuthenticationManager authManager;
//    private final JWTService jwtService;
//    private final ApplicationContext context;
//    private final MemberDTOMapper memberDTOMapper;
//    private final RegisterMapper registerMapper;
//    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(PASSWORD_ROUNDS);

    @Autowired
    public MemberService(MemberRepository memberRepository
//                         AuthenticationManager authManager,
//                         JWTService jwtService,
//                         ApplicationContext context, MemberDTOMapper memberDTOMapper, RegisterMapper registerMapper
    ) {
        super(memberRepository);
//        this.authManager = authManager;
//        this.jwtService = jwtService;
//        this.context = context;
//        this.memberDTOMapper = memberDTOMapper;
//        this.registerMapper = registerMapper;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    @Transactional
    public Integer updatePasswordByEmail(PasswordByEmail passwordByEmail) {
        return entityRepository.updatePasswordByEmail(passwordByEmail.getEmail(), passwordByEmail.getNewPassword());
    }

    @Transactional
    public Integer updateTokenByEmail(TokenByEmail tokenByEmail) {
        return entityRepository.updateTokenByEmail(tokenByEmail.getEmail(), tokenByEmail.getNewToken());
    }

    @Transactional
    public Integer updateTokenByOldToken(TokenByOldToken tokenByOldToken) {
        return entityRepository.updateTokenByOldToken(tokenByOldToken.getOldToken(), tokenByOldToken.getNewToken());
    }

    @Transactional
    public Integer updatePfpImgNameByEmail(PfpNameByEmail pfpNameByEmail) {
        return entityRepository.updatePfpNameByEmail(pfpNameByEmail.getEmail(), pfpNameByEmail.getPfpName());
    }

    public List<Member> findMembersByUsername(String username) {
        return entityRepository.findMembersByUsername(username);
    }
}
