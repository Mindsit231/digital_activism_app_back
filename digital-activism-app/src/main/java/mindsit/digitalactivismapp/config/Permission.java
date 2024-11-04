package mindsit.digitalactivismapp.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),

    AUTHENTICATED_READ("authenticated:read"),
    AUTHENTICATED_WRITE("authenticated:write"),
    AUTHENTICATED_DELETE("authenticated:delete"),
    AUTHENTICATED_UPDATE("authenticated:update");

    private final String permission;
}
