package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.select.ByToken;
import mindsit.digitalactivismapp.model.query.update.PasswordByEmail;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByEmail;
import mindsit.digitalactivismapp.model.query.update.TokenByOldToken;
import mindsit.digitalactivismapp.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController extends EntityController<Member, MemberService> {
    @Autowired
    public MemberController(MemberService memberService) {
        super(memberService, Member.class);
    }

//    @GetMapping("/select-member-by-email/{email}")
//    public ResponseEntity<Member> selectMemberByEmail(@PathVariable("email") String email) {
//        return ResponseEntity.ok(entityService.findByEmail(email));
//    }
//    @PostMapping("/update-password-by-email")
//    public ResponseEntity<Integer> updatePasswordByEmail(@RequestBody PasswordByEmail passwordByEmail) {
//        return ResponseEntity.ok(entityService.updatePasswordByEmail(passwordByEmail));
//    }
//
//    @PostMapping("/update-token-by-email")
//    public ResponseEntity<Integer> updateTokenByEmail(@RequestBody TokenByEmail tokenByEmail) {
//        return ResponseEntity.ok(entityService.updateTokenByEmail(tokenByEmail));
//    }
//
//    @PostMapping("/update-token-by-old-token")
//    public ResponseEntity<Integer> updateTokenByOldToken(@RequestBody TokenByOldToken tokenByOldToken) {
//        return ResponseEntity.ok(entityService.updateTokenByOldToken(tokenByOldToken));
//    }
//
//    @PostMapping("/update-pfp-img-path-by-email")
//    public ResponseEntity<Integer> updatePfpImgPathByEmail(@RequestBody PfpNameByEmail pfpNameByEmail) {
//        return ResponseEntity.ok(entityService.updatePfpImgNameByEmail(pfpNameByEmail));
//    }
//
//    @GetMapping("/find-members-by-username/{username}")
//    public ResponseEntity<List<Member>> findMembersByUsername(@PathVariable String username) {
//        return ResponseEntity.ok(entityService.findMembersByUsername(username));
//    }
}
