package mindsit.digitalactivismapp.mapper.member;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.authentication.regsiter.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RegisterMapper implements Function<RegisterRequest, Member> {
    @Override
    public Member apply(RegisterRequest registerRequest) {
        return new Member(
                registerRequest.username(),
                registerRequest.email()
        );
    }
}
