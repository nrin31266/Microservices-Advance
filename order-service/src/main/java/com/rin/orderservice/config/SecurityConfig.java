package com.rin.orderservice.config;

//import com.rin.orderservice.middleware.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/orders/**").authenticated()
//                        .anyRequest().permitAll())
//                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/orders").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(resource -> resource
                        .jwt(Customizer.withDefaults())
                )
                .build();
    }

//    @Bean
//    public JwtAuthFilter jwtAuthFilter() {
//        return new JwtAuthFilter();
//    }
}
