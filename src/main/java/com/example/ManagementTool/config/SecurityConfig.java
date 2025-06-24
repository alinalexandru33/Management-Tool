package com.example.ManagementTool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin").password("{noop}admin").roles("ADMIN").build(),
            User.withUsername("user").password("{noop}user").roles("USER").build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults());

        return http.build();
    }

    private static org.springframework.security.config.Customizer<org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer<HttpSecurity>> withDefaults() {
        return org.springframework.security.config.Customizer.withDefaults();
    }
}
