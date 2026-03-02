package org.mario.authservice.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class PersistenceConfig {

    /**
     * Devuelve el usuario auditor para la auditoría automática.
     * Ahora mismo ponemos "system" por defecto.
     * Más adelante se puede extraer del token JWT del usuario autenticado.
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("system");
    }
}