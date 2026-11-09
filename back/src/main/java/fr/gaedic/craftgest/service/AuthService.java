package fr.gaedic.craftgest.service;

import fr.gaedic.craftgest.model.User;
import fr.gaedic.craftgest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    // De quoi je vais avoir besoin dans cette classe (tiroirs vides)
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructeur: Je viens remplir le tiroir
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(String mail, String password) {
        // On cherche l'utilisateur par son identifiant son mail
        User user = userRepository
                .findByMail(mail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants incorrects"));
        // Si le mot de passe ne correspond pas au hash, erreur:
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants incorrects");
        }
        // Si le compte n'est pas actif, erreur.
        if (!user.getStatus().equals("ACTIVE")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Compte désactivé, Contactez l'administrateur");
        }
        return user;
    }
}
