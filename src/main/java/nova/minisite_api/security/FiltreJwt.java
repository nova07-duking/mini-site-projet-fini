package nova.minisite_api.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre qui s'exécute une fois par requête pour valider le token JWT.
 * Modifié pour gérer proprement l'expiration (401 Unauthorized).
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
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
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

            // 5. Si l'email est extrait et que l'utilisateur n'est pas déjà authentifié
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.serviceDetailsUtilisateur.loadUserByUsername(userEmail);

                // 6. Si le token est valide (vérification de la signature et de l'expiration)
                if (generateurJwt.estValide(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. Mise à jour du contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // On continue la chaîne normalement si tout va bien
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // Cas spécifique : Le token a expiré (l'erreur que tu as eue)
            handleException(response, "Le token JWT est expiré. Veuillez vous reconnecter.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (SignatureException | MalformedJwtException e) {
            // Cas : Le token a été modifié ou est invalide
            handleException(response, "Token JWT invalide ou corrompu.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            // Erreur générique
            logger.error("Erreur d'authentification : " + e.getMessage());
            handleException(response, "Erreur lors de l'authentification.", HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * Méthode utilitaire pour renvoyer une réponse JSON propre en cas d'erreur dans le filtre.
     */
    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"error\": \"%s\", \"status\": %d}", message, status);
        response.getWriter().write(json);
    }
}