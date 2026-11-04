package fr.gaedic.craftgest.controller;

import fr.gaedic.craftgest.dto.LoginRequest;
import fr.gaedic.craftgest.dto.LoginResponse;
import fr.gaedic.craftgest.model.User;
import fr.gaedic.craftgest.service.AuthService;
import fr.gaedic.craftgest.service.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Cette classe reçoit des requetes HTTP et renvoie du JSON
@RequestMapping("/api/auth") // Début de toutes les routes de la classe
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    //Constructeur :
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login") // La route POST /api/auth/login
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user= authService.login(request.mail(), request.password());
        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
