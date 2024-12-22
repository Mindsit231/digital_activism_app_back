package mindsit.digitalactivismapp.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static mindsit.digitalactivismapp.config.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    PUBLIC(Collections.emptySet()),
    SITE_ADMIN(
            Set.of(
                    SITE_ADMIN_READ,
                    SITE_ADMIN_WRITE,
                    SITE_ADMIN_DELETE,
                    SITE_ADMIN_UPDATE,

                    COMMUNITY_ADMIN_READ,
                    COMMUNITY_ADMIN_WRITE,
                    COMMUNITY_ADMIN_DELETE,
                    COMMUNITY_ADMIN_UPDATE,

                    AUTHENTICATED_READ,
                    AUTHENTICATED_WRITE,
                    AUTHENTICATED_DELETE,
                    AUTHENTICATED_UPDATE
            )
    ),
    AUTHENTICATED(
            Set.of(
                    AUTHENTICATED_READ,
                    AUTHENTICATED_WRITE,
                    AUTHENTICATED_DELETE,
                    AUTHENTICATED_UPDATE
            )
    ),
    COMMUNITY_ADMIN(
            Set.of(
                    COMMUNITY_ADMIN_READ,
                    COMMUNITY_ADMIN_WRITE,
                    COMMUNITY_ADMIN_DELETE,
                    COMMUNITY_ADMIN_UPDATE,

                    AUTHENTICATED_READ,
                    AUTHENTICATED_WRITE,
                    AUTHENTICATED_DELETE,
                    AUTHENTICATED_UPDATE
            )
    ),;


    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
