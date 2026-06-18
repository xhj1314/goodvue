package com.news.config;

import com.news.service.UserService;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.*;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ── 密码加密 Bean ──────────────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── 认证提供者（使用数据库里的 BCrypt 密码） ───────────────────────
    @Bean
    public DaoAuthenticationProvider authProvider(UserService userService,
                                                  PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    // ── CORS：允许前端 5174 端口跨域访问 ──────────────────────────────
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    // ── 安全过滤链 ─────────────────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 公开接口（无需登录）
                .requestMatchers(HttpMethod.GET,  "/news",      "/news/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/register", "/users/login").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其余接口需要 HTTP Basic 认证
                .anyRequest().authenticated()
            )
            // 自定义认证入口：仅对需要认证的接口返回 401，公开接口不拦截
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                String path = request.getRequestURI();
                String method = request.getMethod();
                // 注册、登录、新闻浏览等公开接口不返回 401
                if (path.startsWith("/users/register") || path.startsWith("/users/login")
                        || path.startsWith("/news") || "OPTIONS".equals(method)) {
                    return;
                }
                response.setHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
            }))
            // HTTP Basic 认证（不自动挑战，由下面的 exceptionHandling 统一处理）
            .httpBasic(basic -> basic.authenticationEntryPoint((req, res, e) -> {}));
        return http.build();
    }
}
