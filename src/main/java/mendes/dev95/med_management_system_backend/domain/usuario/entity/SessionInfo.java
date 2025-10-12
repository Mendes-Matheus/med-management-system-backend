package mendes.dev95.med_management_system_backend.domain.usuario.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionInfo implements Serializable {
    private String sessionId;
    private String token;
    private UUID userId;
    private String username;
    private String email;
    private RolesUsuario role;
    private String deviceInfo;
    private String ipAddress;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiresAt;
    private boolean active;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}