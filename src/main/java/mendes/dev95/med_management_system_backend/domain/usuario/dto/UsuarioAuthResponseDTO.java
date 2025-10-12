package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import java.time.Instant;
import java.util.UUID;

public record UsuarioAuthResponseDTO(
        UUID id,
        String username,
        String email,
        String accessToken,
        String refreshToken,
        Instant refreshTokenExpiration
) {}

