package mendes.dev95.med_management_system_backend.infra.security.filter;


import jakarta.servlet.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class HttpsEnforcerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String proto = httpRequest.getHeader("X-Forwarded-Proto");

        if ("http".equalsIgnoreCase(proto)) {
            log.warn("Requisição HTTP bloqueada: {} {}",
                    httpRequest.getMethod(), httpRequest.getRequestURI());

            // Opção 1: Bloquear
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "HTTPS é obrigatório");

            return;
        }
        chain.doFilter(request, response);
    }
}
