package se.callista.workshop.karate.product.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class CorsConfig {

    @Value("${cors.allowed-origins:#null}")
    final String[] allowedOrigins;
    @Value("${cors.allowed-methods:GET,HEAD,POST}")
    final String[] allowedOMethods;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (allowedOrigins != null) {
                    registry.addMapping("/products/**")
                            .allowedOrigins(allowedOrigins)
                            .allowedMethods(allowedOMethods);
                }
            }
        };
    }

}
