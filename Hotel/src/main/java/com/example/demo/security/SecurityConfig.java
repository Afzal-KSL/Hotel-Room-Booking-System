package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomAuthEntryPoint authEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Strong password encoding
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http = http.cors(c -> c.configurationSource(corsConfigurationSource()));
        http = http.csrf(csrf -> csrf.disable());
        http = http.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint));
        http = http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http = http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/hotels", "/hotels/search", "/hotels/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.POST, "/hotels").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/hotels/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/hotels/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/rooms", "/rooms/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.POST, "/rooms").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/rooms/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/rooms/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.GET, "/reservations/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.POST, "/reservations").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.PUT, "/reservations/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/reservations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/guests").hasRole("GUEST")
                .requestMatchers(HttpMethod.GET, "/guests/**","/guests/by-email").hasAnyRole("ADMIN", "GUEST")
                .requestMatchers(HttpMethod.PUT, "/guests/**").hasAnyRole("ADMIN", "GUEST")
                .requestMatchers("/rates/**", "/inventory/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/profile/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers("/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        http = http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    JwtUtil jwtUtil() {
        return new JwtUtil(); // If not already a Spring bean
    }
}