package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import mendes.dev95.med_management_system_backend.domain.usuario.entity.RolesUsuario;

import java.util.UUID;


public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String cidade,
        String telefone,
        String email,
        String username,
        RolesUsuario role
) {}

