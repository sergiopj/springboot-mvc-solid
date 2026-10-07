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

// LA SEGURIDAD ES INFRAESTRUCTURA Y NO DOMINIO
// Habilitamos la seguridad web de Spring Security en esta clase
@Configuration
@EnableWebSecurity // Esta anotación es opcional, pero la dejamos para que quede claro que esta
                   // clase configura seguridad web
public class SecurityConfig {

    // Definimos el bean que configura toda la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Reglas de autorización por endpoint
                .authorizeHttpRequests(auth -> auth

                        // Cuentas: crear y listar todas solo para ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/v1/accounts").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts").hasAuthority("ROLE_ADMIN")

                        // Cuentas: ver una cuenta por ID permite también al USER
                        .requestMatchers(HttpMethod.GET, "/api/v1/accounts/{id}")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // Transacciones: transferir y consultar permite a ADMIN y USER
                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions/transfer")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/{id}/transactions")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // La consola H2 es accesible sin token (solo para desarrollo local)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Cualquier otra ruta exige un JWT válido emitido por Keycloak
                        .anyRequest().authenticated())

                // Activamos la validación de tokens JWT y le indicamos cómo leer los roles de
                // Keycloak
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())))

                // Desactivamos la protección de frames para que funcione la consola H2 en el
                // navegador
                // H2 usa <iframe> internamente y los navegadores los bloquean por defecto
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                // Las APIs REST usan tokens Bearer (no cookies), por eso desactivamos CSRF
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Converter que le enseña a Spring Security dónde están los roles dentro del
    // token JWT de Keycloak.
    // Por defecto Spring busca los roles en el campo "scope", pero Keycloak los
    // pone en "realm_access.roles"
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        // Sobre escribimos cómo se extraen los roles del token JWT
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            // Leemos el campo "realm_access" del token (donde Keycloak guarda los roles)
            var realmAccess = jwt.getClaimAsMap("realm_access");

            // Si no hay roles definidos, devolvemos una lista vacía (sin permisos)
            if (realmAccess == null || !(realmAccess.get("roles") instanceof List<?> roles)) {
                return List.of();
            }

            // Convertimos cada rol a SimpleGrantedAuthority tal como viene de Keycloak.
            // Keycloak ya incluye el prefijo ROLE_ (ej: ROLE_ADMIN), así que no lo añadimos
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        });

        return converter;
    }
}
