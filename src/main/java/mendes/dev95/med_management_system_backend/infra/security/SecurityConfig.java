package mendes.dev95.med_management_system_backend.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/estabelecimentos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pacientes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/procedimentos").permitAll()
                        .requestMatchers(HttpMethod.POST, "/procedimentos-paciente").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/estabelecimentos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pacientes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/procedimentos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/procedimentos-paciente").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/estabelecimentos").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/pacientes").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/procedimentos").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/procedimentos-paciente").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/users").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

