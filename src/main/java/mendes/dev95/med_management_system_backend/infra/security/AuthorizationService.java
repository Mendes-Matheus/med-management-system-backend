package mendes.dev95.med_management_system_backend.infra.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.RolesUsuario;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtém o usuário autenticado atual
     */
    public Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        String username = auth.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Usuário não encontrado"));
    }

    /**
     * Verifica se o usuário tem uma das roles especificadas
     */
    public boolean hasAnyRole(RolesUsuario... roles) {
        Usuario user = getCurrentUser();
        return Arrays.asList(roles).contains(user.getRole());
    }

    /**
     * Exige que o usuário tenha uma das roles especificadas
     * Lança exceção se não tiver
     */
    public void requireAnyRole(RolesUsuario... roles) {
        if (!hasAnyRole(roles)) {
            log.warn("Acesso negado - Usuário {} tentou acessar recurso que requer roles: {}",
                    getCurrentUser().getUsername(),
                    Arrays.toString(roles));
            throw new AccessDeniedException("Permissão insuficiente");
        }
    }

    /**
     * Verifica se o usuário é ADMINISTRADOR
     */
    public boolean isAdmin() {
        return getCurrentUser().getRole() == RolesUsuario.ADMINISTRADOR;
    }

    /**
     * Registra acesso a dados sensíveis para auditoria (LGPD)
     */
    public void logSensitiveDataAccess(String entityType, Object entityId, String action) {
        Usuario user = getCurrentUser();
        log.info("AUDITORIA - Usuário: {}, Role: {}, Ação: {}, Entidade: {}, ID: {}",
                user.getUsername(),
                user.getRole(),
                action,
                entityType,
                entityId
        );
    }
}