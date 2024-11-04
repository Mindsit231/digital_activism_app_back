package mindsit.digitalactivismapp.model.misc;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostBody {
    private Long memberId;
    private List<String> tags;
    private List<Long> excludeIds = new ArrayList<>();
    private int limit = 10;
}
