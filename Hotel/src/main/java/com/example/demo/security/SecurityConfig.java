package com.example.demo.security;

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
        http.csrf(csrf -> csrf.disable())
        	.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/hotels", "/hotels/search", "/hotels/{id}").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.PUT, "/hotels/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/hotels/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/hotels/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/rooms", "/rooms/{id}").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.PUT, "/rooms/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/rooms/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/rooms/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/reservations/**", "/reservations/{id}").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.POST, "/reservations/**").hasAnyRole("ADMIN", "STAFF", "GUEST")
                .requestMatchers(HttpMethod.PUT, "/reservations/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/reservations/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/guests").hasRole("GUEST")
                .requestMatchers("/guests/**").hasRole("ADMIN")
                .requestMatchers("/rates/**", "/inventory/**").hasAnyRole("ADMIN", "STAFF")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    JwtUtil jwtUtil() {
        return new JwtUtil(); // If not already a Spring bean
    }
}