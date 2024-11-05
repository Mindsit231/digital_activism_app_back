package mindsit.digitalactivismapp.model.misc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EmailContainer {
    protected String to;
    protected String subject;
    protected String body;
    protected boolean isHTML;
    protected String template;

    public EmailContainer(String to, String subject, String body, boolean isHTML, String template) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.isHTML = isHTML;
        this.template = template;
    }

    public EmailContainer() {}
}
