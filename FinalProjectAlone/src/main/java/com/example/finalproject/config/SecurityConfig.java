package com.example.finalproject.config;

import com.example.finalproject.service.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;


    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers("/", "/index", "/login","/register/user","api/register/user", "/css/**", "/js/**").permitAll() //frontend
                    .requestMatchers("/api/register").permitAll()
                    .requestMatchers("/api/signin").permitAll()
                    .requestMatchers("/user_role/role").permitAll() // change to has authority ADMIN after database reset.
                    .requestMatchers(HttpMethod.GET, "/product").hasAnyAuthority("ADMIN", "USER")
                    .requestMatchers(HttpMethod.POST, "/product").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/product/").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "product").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/categories").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/categories").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/categories/").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/categories").authenticated()
                    .anyRequest().authenticated();
        }).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

}
