package fr.gaedic.craftgest.config;

import fr.gaedic.craftgest.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer { // Je veux configurer le fonctionnement web de spring
    private final AuthInterceptor authInterceptor;

    // Constructeur:
    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor) // Méthode nommée pour indiquer quelles routes vérifier
                .addPathPatterns("/api/**") // devant ttes les routes qui ont ce pattern
                .excludePathPatterns("/api/auth/login", "/api/auth/logout"); // Sauf la route login et la route logout
    }
}
