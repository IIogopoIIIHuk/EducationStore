//package EduStore.user_service.config;
//
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//
//import java.util.List;
//
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable);
//////              .cors(AbstractHttpConfigurer::disable)
////                .cors(cors -> cors.configurationSource(request -> {
////                    var corsConfiguration = new CorsConfiguration();
////                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
////                    corsConfiguration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
////                    corsConfiguration.setAllowedHeaders(List.of("*"));
////                    corsConfiguration.setAllowCredentials(true);
////                    return corsConfiguration;
////                }))
////                .authorizeRequests(auth -> auth
////                                .requestMatchers(
////                                        "/v3/api-docs/**",
////                                        "/swagger-ui/**",
////                                        "/swagger-ui.html"
////                                ).permitAll()
////                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
////                                .requestMatchers("/api/user/**").hasRole("USER")
//////                              .requestMatchers("/admin").authenticated()
////                                .anyRequest().permitAll()
////                )
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//
//}
