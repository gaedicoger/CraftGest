package fr.gaedic.craftgest.interceptor;

import fr.gaedic.craftgest.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor
        implements HandlerInterceptor { // spring sait qu'il doit utiliser cette classe comme middleware
    // authentification
    private final JwtService jwtService;

    // Constructeur:
    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object Handler) { // Méthode preHandle imposée, spring va l'appeler avant chaque controller
        // Lire l'entête Authorization :
        String header = request.getHeader("Authorization"); // récupère la valeur de l'entête
        if (header == null
                || !header.startsWith("Bearer ")) { // Si le token est manquant ou qu'il ne commence pas par le bearer
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token manquant"); // erreur
        }
        String token = header.substring(7); // garde le texte à partir du 8eme caractère (le bearer en fait 7)

        // Vérifier le token et qui fait la demande:
        try {
            Claims claims = jwtService.parseToken(token);
            request.setAttribute(
                    "userId",
                    Integer.valueOf(
                            claims.getSubject())); // On vient chercher le user id tu token en texte qu'on repasse en
            // nombre
            request.setAttribute(
                    "role", claims.get("role", String.class)); // On récupère la valeur de role sous forme de texte
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
        // Sinon laisser passer :
        return true;
    }
}
