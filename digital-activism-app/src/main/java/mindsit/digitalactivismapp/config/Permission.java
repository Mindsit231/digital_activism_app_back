package mindsit.digitalactivismapp.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    SITE_ADMIN_READ("site_admin:read"),
    SITE_ADMIN_WRITE("site_admin:write"),
    SITE_ADMIN_DELETE("site_admin:delete"),
    SITE_ADMIN_UPDATE("site_admin:update"),

    AUTHENTICATED_READ("authenticated:read"),
    AUTHENTICATED_WRITE("authenticated:write"),
    AUTHENTICATED_DELETE("authenticated:delete"),
    AUTHENTICATED_UPDATE("authenticated:update"),

    COMMUNITY_ADMIN_READ("community_admin:read"),
    COMMUNITY_ADMIN_WRITE("community_admin:write"),
    COMMUNITY_ADMIN_DELETE("community_admin:delete"),
    COMMUNITY_ADMIN_UPDATE("community_admin:update");

    private final String permission;
}
