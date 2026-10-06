package com.atlas.bank.atlas_bank.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

// Habilitamos la seguridad web de Spring Security en esta clase
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectamos la URL del emisor del token (Keycloak) desde el application.yaml
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    // Definimos el bean que configura toda la cadena de filtros de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Las APIs REST usan tokens Bearer (no cookies), por eso desactivamos CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Configuramos las sesiones como STATELESS: sin guardar sesión en el servidor.
                // Cada petición debe traer su propio token JWT para autenticarse
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de autorización para cada endpoint
                .authorizeHttpRequests(auth -> auth
                        // La consola H2 es accesible sin token (solo para desarrollo local)
                        .requestMatchers("/h2-console/**").permitAll()
                        // Cualquier otra ruta exige un JWT válido emitido por Keycloak
                        .anyRequest().authenticated()
                )

                // Activamos el modo de servidor de recursos OAuth2 con validación de tokens JWT.
                // Spring Boot usará el JwtDecoder para verificar que los tokens son auténticos
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))

                .build();
    }

    // Bean que construye el decodificador de tokens JWT usando la URL de Keycloak
    @Bean
    public JwtDecoder jwtDecoder() {
        // Keycloak publica sus claves públicas en: {issuer-uri}/protocol/openid-connect/certs
        // NimbusJwtDecoder las descarga automáticamente y verifica la firma del token
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }
}
