package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PasswordByEmail;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByOldToken;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
