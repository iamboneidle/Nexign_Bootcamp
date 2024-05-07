package crm.crm_service.CRMSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Класс-конфигуратор системы ролей сервиса.
 */
@Configuration
@EnableWebSecurity
public class CRMSecurityConfig {
    /**
     * URL-адрес frontend'а, с которого отправляются запросы.
     */
    @Value("${frontend.cors.url}")
    private String frontendCorsUrl;
    /**
     * Сервис по поиску пользователей в базе данных.
     */
    @Autowired
    private CRMDetailsService CRMDetailsService;

    /**
     * Метод для возвращения кодировщика паролей пользователей и админа.
     *
     * @return BCryptPasswordEncoder кодировщик.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Метод для Аутентификации и авторизации пользователей, обращающихся на те или иные URL-адреса.
     *
     * @param http http.
     * @return SecurityFilterChain с подтверждением доступа.
     * @throws Exception при ошибке валидации.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(CRMDetailsService)
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/user").hasAuthority("SUBSCRIBER");
                    authConfig.requestMatchers(HttpMethod.POST, "/user/put-money").hasAuthority("SUBSCRIBER");
                    authConfig.requestMatchers(HttpMethod.GET, "/admin").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/save").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/change-tariff").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/change-tariff-monthly").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/put-money").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/put-money-monthly").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/post-tariffs").hasAuthority("MANAGER");
                    authConfig.anyRequest().authenticated();
                })
                .formLogin(withDefaults())
                .cors(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Бин конфигурации CORS.
     *
     * @return Конфигурация CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontendCorsUrl);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Метод, предназначенный для определения адресов, не требующих авторизации.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .requestMatchers(
                        "/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/api-docs/**"
                );
    }
}