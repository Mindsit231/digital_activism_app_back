package mindsit.digitalactivismapp.model.misc;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String to;
    private String subject;
    private String body;
    @JsonAlias("is_html")
    private boolean isHTML;
    private String template;
}
