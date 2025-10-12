package mendes.dev95.med_management_system_backend.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
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
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioRepository userRepository;

    private static final int MAX_TOKEN_LENGTH = 4096; // proteção simples

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        // caminho público -> skip (ex: login, health, docs)
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

        // tenta extrair authorities do token
        List<String> authoritiesClaim = decoded.getClaim("authorities").asList(String.class);

        Collection<GrantedAuthority> authorities = Collections.emptyList();
        if (authoritiesClaim != null && !authoritiesClaim.isEmpty()) {
            authorities = authoritiesClaim.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    subject, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // fallback: buscar no DB (menos performático)
            var usuarioOpt = userRepository.findByEmail(subject);
            if (usuarioOpt.isEmpty()) {
                // para segurança, responda 401 (não revelar se email existe)
                unauthorized(response, "user.notfound");
                return;
            }
            Usuario usuario = usuarioOpt.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());
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
