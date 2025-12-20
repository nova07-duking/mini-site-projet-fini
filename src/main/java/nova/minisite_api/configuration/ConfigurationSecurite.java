package nova.minisite_api.configuration;

import nova.minisite_api.security.FiltreJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize dans les contrôleurs
public class ConfigurationSecurite {

    private final FiltreJwt filtreJwt;

    public ConfigurationSecurite(FiltreJwt filtreJwt) {
        this.filtreJwt = filtreJwt;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Documentation et Authentification (Public)
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // 2. Produits & Catégories : Lecture publique
                        .requestMatchers(HttpMethod.GET, "/api/produits/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 3. Produits & Catégories : Écriture (ADMIN uniquement)
                        // Utiliser hasAnyAuthority permet d'ignorer le besoin du préfixe ROLE_
                        .requestMatchers(HttpMethod.POST, "/api/produits/**", "/api/categories/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/produits/**", "/api/categories/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/produits/**", "/api/categories/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 4. Autres routes
                        .requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers("/api/client/**").hasAnyAuthority("CLIENT", "ROLE_CLIENT")

                        // 5. Tout le reste requiert une simple authentification
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtreJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}