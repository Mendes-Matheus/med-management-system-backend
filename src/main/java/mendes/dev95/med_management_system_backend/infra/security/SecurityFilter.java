package mendes.dev95.med_management_system_backend.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    private static final int MAX_TOKEN_LENGTH = 4096;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        // Caminho público -> skip
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token.length() > MAX_TOKEN_LENGTH) {
            unauthorized(response, "token.too.long");
            return;
        }

        var decoded = tokenService.validateAndGetDecoded(token);
        if (decoded == null) {
            unauthorized(response, "token.invalid");
            return;
        }

        String subject = decoded.getSubject();
        if (subject == null) {
            unauthorized(response, "token.invalid");
            return;
        }

        // ✅ SEMPRE usar authorities do token (elimina consulta ao banco)
        List<String> authoritiesClaim = decoded.getClaim("authorities").asList(String.class);

        if (authoritiesClaim != null && !authoritiesClaim.isEmpty()) {
            Collection<GrantedAuthority> authorities = authoritiesClaim.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // ✅ Se não houver authorities no token, tratar como sem permissões
            log.warn("Token sem authorities para usuário: {}", subject);
            Collection<GrantedAuthority> authorities = Collections.emptyList();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        if (!authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7).trim();
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (path.equals("/auth/login") && "POST".equalsIgnoreCase(method)) return true;
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) return true;
        if (path.equals("/actuator/health")) return true;
        return false;
    }

    private void unauthorized(HttpServletResponse response, String messageKey) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        String json = String.format("{\"error\": \"%s\"}", messageKey);
        response.getWriter().write(json);
    }
}
