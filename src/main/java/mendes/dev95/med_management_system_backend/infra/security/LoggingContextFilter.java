package mendes.dev95.med_management_system_backend.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingContextFilter extends OncePerRequestFilter {

    private static final String MDC_USER = "usuario";
    private static final String MDC_IP = "ip";
    private static final String MDC_PATH = "path";
    private static final String MDC_REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String requestId = request.getHeader("X-Request-Id");
            if (requestId == null || requestId.isBlank()) requestId = UUID.randomUUID().toString();
            MDC.put(MDC_REQUEST_ID, requestId);

            MDC.put(MDC_IP, request.getRemoteAddr());
            MDC.put(MDC_PATH, request.getRequestURI());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                Object principal = auth.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
                    MDC.put(MDC_USER, ud.getUsername());
                } else if (principal instanceof String) {
                    MDC.put(MDC_USER, (String) principal);
                } else {
                    MDC.put(MDC_USER, principal.toString());
                }
            } else {
                MDC.put(MDC_USER, "anônimo");
            }

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}


