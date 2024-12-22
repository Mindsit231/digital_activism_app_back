package mindsit.digitalactivismapp.config;

import mindsit.digitalactivismapp.filter.JWTFilter;
import mindsit.digitalactivismapp.service.member.MemberDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static mindsit.digitalactivismapp.config.Role.*;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    public final static int PASSWORD_ROUNDS = 12;
    private final MemberDetailsService memberDetailsService;
    private final JWTFilter jwtFilter;

    @Value("${custom.url.frontend:}")
    private String frontendUrl;

    public SecurityConfig(MemberDetailsService memberDetailsService, JWTFilter jwtFilter) {
        this.memberDetailsService = memberDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .cors(customizer ->
                        customizer.configurationSource(request -> {
                            CorsConfiguration cors = new CorsConfiguration();
                            cors.setAllowedOrigins(List.of(frontendUrl));
                            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            cors.setAllowedHeaders(List.of("*"));
                            cors.setAllowCredentials(true);
                            return cors;
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("public/**").permitAll()

                        .requestMatchers("authenticated/**").hasAnyRole(AUTHENTICATED.name(), SITE_ADMIN.name(), COMMUNITY_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "authenticated/**").hasAnyRole(AUTHENTICATED.name(), SITE_ADMIN.name(), COMMUNITY_ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "authenticated/**").hasAnyRole(AUTHENTICATED.name(), SITE_ADMIN.name(), COMMUNITY_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "authenticated/**").hasAnyRole(AUTHENTICATED.name(), SITE_ADMIN.name(), COMMUNITY_ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "authenticated/**").hasAnyRole(AUTHENTICATED.name(), SITE_ADMIN.name(), COMMUNITY_ADMIN.name())

                        .requestMatchers("authenticated/community-admin/**").hasAnyRole(COMMUNITY_ADMIN.name(), SITE_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "authenticated/community-admin/**").hasAnyRole(COMMUNITY_ADMIN.name(), SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "authenticated/community-admin/**").hasAnyRole(COMMUNITY_ADMIN.name(), SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "authenticated/community-admin/**").hasAnyRole(COMMUNITY_ADMIN.name(), SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "authenticated/community-admin/**").hasAnyRole(COMMUNITY_ADMIN.name(), SITE_ADMIN.name())

                        .requestMatchers("admin/**").hasRole(SITE_ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "admin/**").hasRole(SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "admin/**").hasRole(SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "admin/**").hasRole(SITE_ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "admin/**").hasRole(SITE_ADMIN.name())

                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(PASSWORD_ROUNDS));
        provider.setUserDetailsService(memberDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
