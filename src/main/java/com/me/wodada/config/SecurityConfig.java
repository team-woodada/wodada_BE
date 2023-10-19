package com.me.wodada.config;

import com.me.wodada.auth.handler.LoginFailureHandler;
import com.me.wodada.auth.handler.LoginSuccessHandler;
import com.me.wodada.auth.jwt.filter.JwtAuthenticationProcessingFilter;
import com.me.wodada.auth.jwt.filter.JwtExceptionFilter;
import com.me.wodada.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public WebSecurityCustomizer customizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http
                .httpBasic(basic -> basic.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(login -> login.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.antMatchers("/token/**").permitAll()
                                .antMatchers("/auth/token").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .antMatchers("/test").authenticated()
                                .antMatchers("/api/member/**").authenticated()
                                .anyRequest().permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/"))
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                        .userInfoEndpoint().userService(customOAuth2UserService)
                );

        return http
                .addFilterBefore(jwtAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationProcessingFilter.class)
                .build();
    }
}
