package io.github.tlsdla1235.seniormealplan.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthFilter f, UserDetailsService uds, PasswordEncoder pe) {
        this.jwtAuthFilter = f; this.userDetailsService = uds; this.passwordEncoder = pe;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(passwordEncoder);
        return p;
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // JWT이므로 CSRF 비활성화 + 세션 미사용
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//
//                // 예외 응답 형식(401/403)을 JSON으로
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((req, res, e) -> { // 401
//                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            res.setContentType("application/json;charset=UTF-8");
//                            res.getWriter().write("{\"error\":\"unauthorized\"}");
//                        })
//                        .accessDeniedHandler((req, res, e) -> { // 403
//                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                            res.setContentType("application/json;charset=UTF-8");
//                            res.getWriter().write("{\"error\":\"forbidden\"}");
//                        })
//                )
//
//                // 권한 규칙
//                .authorizeHttpRequests(auth -> auth
//                        // 프리플라이트 허용
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                        // 인증 열어둘 경로 (회원가입/로그인)
//                        .requestMatchers("/api/auth/**").permitAll()
//
//                        // Swagger / OpenAPI
//                        .requestMatchers(
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html"
//                        ).permitAll()
//
//                        // (선택) 정적 리소스 및 헬스체크
//                        .requestMatchers(
//                                "/", "/index.html", "/assets/**", "/css/**", "/js/**", "/actuator/health"
//                        ).permitAll()
//
//                        // (선택) H2 콘솔 사용 시
//                        //.requestMatchers("/h2-console/**").permitAll()
//
//                        // 그 외는 인증 필요
//                        .anyRequest().authenticated()
//                )
//
//                // H2 콘솔을 쓸 경우에만 필요
//                //.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//
//                // DaoAuthenticationProvider 등록
//                .authenticationProvider(daoAuthProvider())
//
//                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 배치
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//
//                // CORS(기본값). 필요 시 CorsConfigurationSource 빈으로 상세 설정
//                .cors(Customizer.withDefaults());
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"unauthorized\"}");
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"forbidden\"}");
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/", "/index.html", "/assets/**", "/css/**", "/js/**", "/actuator/health").permitAll()
                        .requestMatchers("/api/admin/**").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());

        return http.build();
    }
}