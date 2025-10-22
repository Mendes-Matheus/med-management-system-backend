package mendes.dev95.med_management_system_backend.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;
    @Autowired
    private final LoggingContextFilter loggingContextFilter;
    @Autowired
    private final Environment env;
    @Autowired
    private final SimpleRateLimitingFilter simpleRateLimitingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/users").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/auth/users").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/id/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/email/{email}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/estabelecimentos").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/estabelecimentos/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.POST, "/estabelecimentos").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.PUT, "/estabelecimentos/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/estabelecimentos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/procedimentos").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/procedimentos/estabelecimento/{estabelecimentoId}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.POST, "/procedimentos").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.POST, "/procedimentos/{id}/estabelecimentos/{estabelecimentoId}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.PUT, "/procedimentos/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/procedimentos/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/procedimentos/{id}/estabelecimentos/{estabelecimentoId}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/pacientes").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/pacientes/nome/{nome}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/pacientes/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/pacientes/cpf/{cpf}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/pacientes/procedimentos/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.POST, "/pacientes").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.PUT, "/pacientes/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/procedimentos-paciente").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/procedimentos-paciente/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/procedimentos-paciente/paciente/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.GET, "/procedimentos-paciente/procedimento/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.POST, "/procedimentos-paciente").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.PUT, "/procedimentos-paciente/{id}").hasAnyRole("ADMINISTRADOR", "ASSISTENTE_ADMINISTRATIVO")
                        .requestMatchers(HttpMethod.DELETE, "/procedimentos-paciente/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(simpleRateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(loggingContextFilter, SecurityFilter.class);
        http.addFilterBefore(simpleRateLimitingFilter, SecurityFilter.class);
        http.addFilterAfter(loggingContextFilter, SecurityFilter.class);

        // Forçar HTTPS em produção: usa propriedade "api.security.require-https"
        boolean requireHttps = Boolean.parseBoolean(env.getProperty("api.security.require-https", "false"));
        if (requireHttps) {
            http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${api.cors.allowed-origins:http://localhost:3000}") String allowedOrigins
    ) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

