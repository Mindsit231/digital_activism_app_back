package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.modelDTO.reCaptcha.ReCaptchaRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ReCaptchaService {
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public ResponseEntity<String> verifyCaptcha(ReCaptchaRequest reCaptchaRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(RECAPTCHA_VERIFY_URL)
                .queryParam("secret", reCaptchaRequest.secret())
                .queryParam("response", reCaptchaRequest.response())
                .queryParam("remoteIp", reCaptchaRequest.remoteIp());


        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                null,
                String.class
        );
    }
}
