package mindsit.digitalactivismapp.model.query.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PfpNameByEmail {
    private String email;
    private String pfpName;

    public PfpNameByEmail(String email, String pfpName) {
        this.email = email;
        this.pfpName = pfpName;
    }
}
