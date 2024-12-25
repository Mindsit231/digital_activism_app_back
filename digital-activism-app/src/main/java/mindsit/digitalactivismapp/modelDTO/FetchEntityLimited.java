package mindsit.digitalactivismapp.modelDTO;

public record FetchEntityLimited(Integer limit,
                                 Integer offset,
                                 Long optionalId,
                                 String searchValue) {

}
