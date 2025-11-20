package com.cursosplatform.infrastructure.config;

import com.cursosplatform.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// IMPORTACIONES PARA CORS (NUEVAS)
import org.springframework.web.filter.CorsFilter; 
import org.springframework.web.cors.CorsConfiguration; 
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
// FIN DE IMPORTS PARA CORS

// Estas importaciones se eliminaron:
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

private final JwtAuthenticationFilter jwtAuthenticationFilter;
private final UserDetailsService userDetailsService;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
.csrf(csrf -> csrf.disable())
.authorizeHttpRequests(auth -> auth
 .requestMatchers("/api/auth/**").permitAll()
 .requestMatchers(HttpMethod.GET, "/api/cursos/**").permitAll()
.requestMatchers("/api/admin/**").hasRole("ADMIN")
 .anyRequest().authenticated()
 )
 .sessionManagement(session -> session
 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
 )
 .authenticationProvider(authenticationProvider())
 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

 return http.build();
}

 @Bean
 public PasswordEncoder passwordEncoder() {
 return new BCryptPasswordEncoder();
 }

@Bean
 public AuthenticationProvider authenticationProvider() {
DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 authProvider.setUserDetailsService(userDetailsService);
authProvider.setPasswordEncoder(passwordEncoder());
 return authProvider;
}

@Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
return config.getAuthenticationManager();
}

    // SOLUCIÓN FINAL: FILTRO CORS DE ALTA PRIORIDAD
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // ** CONFIGURACIÓN DE ORIGENES CON COMODÍN DE VERCEL **
        // Permite cualquier subdominio de Vercel, lo que soluciona el problema de los IDs aleatorios
        config.addAllowedOrigin("https://*.vercel.app"); 
        
        // Si quieres permitir también las pruebas locales (opcional)
        config.addAllowedOrigin("http://localhost:3000"); 

        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        
        // Establece la prioridad ALTA (0) para que se ejecute antes que los filtros de Spring Security
        bean.setOrder(0); 
        return bean;
    }
}