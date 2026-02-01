package org.mario.authservice.authservice.configuration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Clase CustomJwtAuthenticationConverter que extiende de {@link JwtAuthenticationConverter}.
 * Esta clase personalizada redefine el proceso de conversión de los atributos de un token JWT a
 * una lista de autoridades de tipo {@link GrantedAuthority}. Específicamente, extrae los roles personalizados
 * del token JWT desde la reclamación "realm_access" -> "roles" y los convierte en autoridades con un prefijo
 * "ROLE_".
 * <p>
 * Utiliza un conversor por defecto proporcionado por {@link JwtGrantedAuthoritiesConverter} para procesar
 * los roles estándar definidos en el JWT y combina esos roles con los roles adicionales del
 * acceso al "realm" especificado.
 * <p>
 * El resultado es una colección de objetos {@link GrantedAuthority} que incluye tanto roles estándar
 * como roles personalizados, y está diseñada para integrarse con el sistema de seguridad de
 * autenticación basada en JWT de Spring Security.
 * <p>
 * Elementos destacados:
 * - La reclamación específica "realm_access" -> "roles" se procesa para añadir roles adicionales.
 * - Los roles personalizados reciben el prefijo "ROLE_" para cumplir con las convenciones de Spring Security.
 */
public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    /**
     * Constructor de la clase {@code CustomJwtAuthenticationConverter}.
     * <p>
     * Este constructor configura un proceso personalizado para la conversión de los claims
     * presentes en un token JWT a una colección de autoridades de Spring Security
     * ({@link GrantedAuthority}). Utiliza un enfoque que combina roles estándar del token
     * con roles adicionales definidos dentro de una reclamación específica del mismo.
     * <p>
     * Detalles del funcionamiento:
     * - Utiliza un conversor por defecto proporcionado por {@link JwtGrantedAuthoritiesConverter}
     * para procesar los roles estándar del token.
     * - Procesa la reclamación personalizada "realm_access" -> "roles". Si existe,
     * extrae una lista de roles específicos y les añade un prefijo "ROLE_".
     * - Combina las autoridades derivadas de los roles estándar y los roles adicionales,
     * consolidándolos en una única colección de {@link GrantedAuthority}.
     * <p>
     * Esto permite extender la funcionalidad predeterminada de Spring Security para incluir
     * roles personalizados definidos en el contexto del "realm" del token JWT.
     */
    public CustomJwtAuthenticationConverter() {
        setJwtGrantedAuthoritiesConverter(jwt -> {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);
            var realmAccess = (jwt.getClaims().get(REALM_ACCESS_CLAIM) instanceof Map<?, ?> map) ? map.get(ROLES_CLAIM) : null;
            if (realmAccess instanceof List<?> roleList) {
                List<SimpleGrantedAuthority> realmRoles = roleList.stream().map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role)).toList();
                authorities.addAll(realmRoles);
            }
            return authorities;
        });
    }
}