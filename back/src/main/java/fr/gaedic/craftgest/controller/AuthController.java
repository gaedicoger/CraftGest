package fr.gaedic.craftgest.controller;

import fr.gaedic.craftgest.dto.LoginRequest;
import fr.gaedic.craftgest.dto.MeResponse;
import fr.gaedic.craftgest.model.User;
import fr.gaedic.craftgest.service.AuthService;
import fr.gaedic.craftgest.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController // Cette classe reçoit des requetes HTTP et renvoie du JSON
@RequestMapping("/api/auth") // Début de toutes les routes de la classe
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    // Constructeur :
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login") // La route POST /api/auth/login
    public ResponseEntity<MeResponse> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.mail(), request.password());
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true) //Ne peut pas être lu par un script JS
                .secure(true) //Envoyé seulement en https
                .sameSite("Strict") //Par mon site uniquement
                .path("/") //Pour toutes les requetes du site
                .maxAge(Duration.ofMillis(jwtService.getExpiration())) //Durée de vie du cookie, calquée sur celle du token présente dans le yaml
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MeResponse(user.getId(), user.getRole()));
    }

    @GetMapping("/me") // la route GET /api/auth/me qui vérifie qui est connecté
    public MeResponse me(@RequestAttribute("userId") Integer userId, @RequestAttribute("role") String role) {
        return new MeResponse(userId, role);
    }
}
