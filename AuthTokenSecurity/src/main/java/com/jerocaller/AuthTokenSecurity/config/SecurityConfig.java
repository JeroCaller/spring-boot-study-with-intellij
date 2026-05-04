package com.jerocaller.AuthTokenSecurity.config;

import com.jerocaller.AuthTokenSecurity.jwt.JwtExceptionFilter;
import com.jerocaller.libs.spoonsuits.web.jwt.DefaultJwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 어노테이션을 사용하기 위함.
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] permitAllUris = {
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/api/auth/**",
        "/api/users/auth/status/**"
    };
    private final JwtExceptionFilter jwtExceptionFilter;
    private final DefaultJwtAuthenticationFilter defaultJwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * <p>참고사항</p>
     * <p>
     *     URI 경로에 "*" 사용 시 한 레벨의 경로만 매칭한다. 반면 "**" 사용 시
     *     하위의 모든 레벨의 경로를 매칭한다.
     * </p>
     * <ul>
     * <li>
     *     예1) `/api/users/*`의 경우 `/api/users/someone`은 매칭되지만,
     *     `/api/users/someone/profile`은 매칭되지 않음.
     * </li>
     * <li>
     *     예2) `/api/users/**`의 경우, `/api/users/someone`,
     *     `/api/users/someone/profile` 모두와 매칭.
     * </li>
     * </ul>
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllUris).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                defaultJwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(
                jwtExceptionFilter,
                defaultJwtAuthenticationFilter.getClass()
            )
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authenticationEntryPoint)
            );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }
}
