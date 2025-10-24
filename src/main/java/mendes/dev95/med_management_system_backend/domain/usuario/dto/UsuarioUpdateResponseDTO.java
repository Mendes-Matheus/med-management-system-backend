package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import jakarta.validation.constraints.*;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.RolesUsuario;

public record UsuarioUpdateResponseDTO(
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String cidade,
        String telefone,
        String email,
        String username,
        String password,
        RolesUsuario role
) {
}
