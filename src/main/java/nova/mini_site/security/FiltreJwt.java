package nova.mini_site.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre qui s'exécute une fois par requête pour valider le token JWT.
 */
@Component
public class FiltreJwt extends OncePerRequestFilter {

    private final GenerateurJwt generateurJwt;
    private final ServiceDetailsUtilisateur serviceDetailsUtilisateur;

    public FiltreJwt(GenerateurJwt generateurJwt,
                     ServiceDetailsUtilisateur serviceDetailsUtilisateur) {
        this.generateurJwt = generateurJwt;
        this.serviceDetailsUtilisateur = serviceDetailsUtilisateur;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 1. Récupération du header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Vérification : Si pas de Bearer, on passe au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() < 8) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Extraction du token (après "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            // 4. Extraction de l'email depuis le token
            final String userEmail = generateurJwt.extraireEmail(jwt);

            // 5. Si l'email est extrait et que l'utilisateur n'est pas déjà authentifié dans le contexte
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // On charge les détails de l'utilisateur depuis la base de données
                UserDetails userDetails = this.serviceDetailsUtilisateur.loadUserByUsername(userEmail);

                // 6. Si le token est valide par rapport aux infos de l'utilisateur
                if (generateurJwt.estValide(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // On enrichit l'authentification avec les détails de la requête HTTP
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. On met à jour le contexte de sécurité de Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // En cas d'erreur de parsing (token expiré ou malformé), on peut logger ici
            // mais on laisse la chaîne continuer (l'accès sera refusé par SecurityConfig)
            logger.error("Impossible de définir l'authentification de l'utilisateur : " + e.getMessage());
        }

        // 8. On continue la chaîne de filtres
        chain.doFilter(request, response);
    }
}