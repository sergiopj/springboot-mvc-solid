package com.atlas.bank.atlas_bank.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

// Configuración de seguridad web y control de accesos con OAuth2 / Keycloak
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Reglas de autorización por endpoint
                .authorizeHttpRequests(auth -> auth
                        // Cuentas: creación y listado completo solo para ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts").hasAuthority("ADMIN")

                        // Cuentas: consulta por ID permitida a ADMIN y USER
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/{id}")
                        .hasAnyAuthority("ADMIN", "USER")

                        // Transacciones: transferencias y consultas permitidas a ADMIN y USER
                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions/transfer")
                        .hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/{id}/transactions")
                        .hasAnyAuthority("ADMIN", "USER")

                        // Consola H2 accesible sin token (solo entorno de desarrollo)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Cualquier otra ruta requiere token JWT válido
                        .anyRequest().authenticated())

                // Servidor de recursos OAuth2 con JWT y extracción de roles
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))

                // Permite iframes para visualizar la consola H2
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // Desactiva CSRF (la API REST usa tokens Bearer sin cookies de sesión)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Convierte los roles de Keycloak (realm_access.roles) a autoridades de Spring
    // Security
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var realmAccess = jwt.getClaimAsMap("realm_access");

            if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
                return List.of();
            }

            // Los roles ya incluyen el prefijo ROLE_ (ej: ADMIN)
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        });

        return converter;
    }
}
