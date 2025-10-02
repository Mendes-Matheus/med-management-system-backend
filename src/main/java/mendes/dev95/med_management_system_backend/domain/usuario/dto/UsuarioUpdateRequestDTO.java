package mendes.dev95.med_management_system_backend.domain.usuario.dto;

public record UsuarioUpdateRequestDTO(
        String username,
        String email,
        String password
) {}
