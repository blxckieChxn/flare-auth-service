package org.mario.authservice.authservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityFilterConfig {
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/v1/json-schema",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**",
            "/api/v1/auth/**"
    };
    /**
     * Configura la cadena de filtros de seguridad de Spring Security para la aplicación.
     * Este metodo define las reglas de autorización, deshabilita la protección CSRF
     * y añade soporte para servidores de recursos OAuth2 con autenticación basada en JWT.
     *
     * @param http instancia de {@code HttpSecurity} utilizada para configurar la seguridad HTTP,
     *        incluyendo las reglas de control de acceso, habilitación/deshabilitación de protecciones
     *        y otras configuraciones relacionadas con seguridad.
     * @return una instancia configurada de {@code SecurityFilterChain} que define la cadena de
     *         filtros de seguridad según las reglas especificadas.
     * @throws Exception si ocurre algún error durante el proceso de configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())));
        return http.build();
    }
}