package fr.gaedic.craftgest.service;

import fr.gaedic.craftgest.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expiration;

    // Constructeur:
    public JwtService(
            @Value("${jwt.secret}") String secret, // Récupére le JWT secret à partir du fichier application.yaml
            @Value("${jwt.expiration}") long expiration) { // Idem pour le temps d'expiration
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // Transforme le secret en signature
        this.expiration = expiration;
    }

    public String generateToken(User user) {
        return Jwts.builder() // Pour démarrer la construction du token
                .subject(String.valueOf(user.getId())) // récupérer l'id de l'utilisateur
                .claim("role", user.getRole()) // récupérer le role de l'utilisateur
                .issuedAt(new Date()) // créer maintenant
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Expire dans "maintenant + 1h"
                .signWith(key) // Signature avec la clé secrète
                .compact(); // Emballe le tout dans une chaîne de caractère
    }

    public Claims parseToken(String token) {
        return Jwts.parser() // Prépare un lecteur de token
                .verifyWith(key) // vérifie la signature avec notre clé
                .build() // Lecteur prêt
                .parseSignedClaims(
                        token) // lit le token et vérifie la signature et la date d'expiration, lance une erreur si faux
                // ou expiré
                .getPayload(); // renvoie le contenu : id et role
    }

    public long getExpiration() {
        return expiration;
    }
}
