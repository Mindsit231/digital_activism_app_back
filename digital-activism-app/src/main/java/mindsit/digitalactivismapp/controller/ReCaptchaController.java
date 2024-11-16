package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.modelDTO.reCaptcha.ReCaptchaRequest;
import mindsit.digitalactivismapp.service.ReCaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReCaptchaController {

    private final ReCaptchaService reCaptchaService;

    public ReCaptchaController(ReCaptchaService reCaptchaService) {
        this.reCaptchaService = reCaptchaService;
    }

    @PostMapping("/public/verify-re-captcha")
    public ResponseEntity<String> verifyReCaptcha(@RequestBody ReCaptchaRequest reCaptchaRequest) {
        return reCaptchaService.verifyCaptcha(reCaptchaRequest);
    }
}
