package crm.crm_service.CRMSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class CRMSecurityConfig {
    @Autowired
    private CRMDetailsService myUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(myUserDetailsService)
                .authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET, "/user").hasAuthority("SUBSCRIBER");
                    authConfig.requestMatchers(HttpMethod.GET, "/admin").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/change-tariff").hasAuthority("MANAGER");
                    authConfig.requestMatchers(HttpMethod.POST, "/admin/post-tariffs").hasAuthority("MANAGER");
                    authConfig.anyRequest().authenticated();
                })
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }
}