package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	http
        .authorizeHttpRequests((requests) -> requests
        	.requestMatchers("/", "/home", "/login", "/registro", "/procesarRegistro", "/acceso-denegado", "/cerrarSesion", "/css/**", "/fonts/**", "/img/**", "/js/**").permitAll() // Rutas públicas // Rutas públicas
            .requestMatchers("/jugadores/**", "/crear", "/editar/**", "/detalles/**").hasRole("ADMIN") // Solo ADMIN
            .requestMatchers("/home", "/crafteos").hasRole("USER") // Solo USER
            .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
        )
        .formLogin((form) -> form
                .loginPage("/login") // Página de login personalizada
                .successHandler(customAuthenticationSuccessHandler) // Usar el success handler personalizado
                .permitAll()
        )
        .logout((logout) -> logout
            .logoutUrl("/cerrarSesion") // Ruta para cerrar sesión
            .logoutSuccessUrl("/login?logout") // Redirigir después del logout
            .invalidateHttpSession(true) // Invalidar la sesión
            .deleteCookies("JSESSIONID") // Eliminar cookies
            .addLogoutHandler((request, response, authentication) -> {
                System.out.println("Usuario ha cerrado sesión: " + authentication.getName());
            })
            .permitAll()
        )
        .exceptionHandling((exception) -> exception
            .accessDeniedPage("/acceso-denegado") // Página de acceso denegado
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}