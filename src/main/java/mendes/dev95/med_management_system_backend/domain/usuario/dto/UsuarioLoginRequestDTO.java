package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


public record UsuarioLoginRequestDTO(

        //@Schema(description = "E-mail do usuário", example = "usuario@email.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        //@Schema(description = "Senha do usuário", example = "123456")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String password
) {}
