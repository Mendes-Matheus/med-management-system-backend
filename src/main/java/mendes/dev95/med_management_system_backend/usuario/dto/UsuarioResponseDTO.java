package mendes.dev95.med_management_system_backend.usuario.dto;

import java.util.UUID;


public record UsuarioResponseDTO(

    //@Schema(description = "Identificador único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    //@Schema(description = "Nome de usuário", example = "matheus95")
    String username,

    //@Schema(description = "E-mail do usuário", example = "usuario@email.com")
    String email,

    //@Schema(description = "Token JWT de autenticação")
    String token

) {}

