package mindsit.digitalactivismapp.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static mindsit.digitalactivismapp.config.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {
    PUBLIC(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_WRITE,
                    ADMIN_DELETE,
                    ADMIN_UPDATE,

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
    );


    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }
}
