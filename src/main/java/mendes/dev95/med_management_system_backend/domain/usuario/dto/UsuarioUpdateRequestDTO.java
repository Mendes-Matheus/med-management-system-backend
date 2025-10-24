package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import jakarta.validation.constraints.*;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.RolesUsuario;

public record UsuarioUpdateRequestDTO(

        //@Schema(description = "Nome completo do usuário", example = "Matheus Mendes")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        //@Schema(description = "CPF do usuário", example = "12345678900")
        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
        String cpf,

        //@Schema(description = "Logradouro", example = "Rua das Flores")
        @NotBlank(message = "O logradouro é obrigatório")
        String logradouro,

        //@Schema(description = "Número da residência", example = "123")
        @NotBlank(message = "O número é obrigatório")
        String numero,

        //@Schema(description = "Complemento", example = "Apto 201")
        String complemento,

        //@Schema(description = "Cidade", example = "São Paulo")
        @NotBlank(message = "A cidade é obrigatória")
        String cidade,

        //@Schema(description = "Telefone do usuário")
        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        String telefone,

        //@Schema(description = "E-mail do usuário")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        //@Schema(description = "Username para login")
        @NotBlank(message = "O username é obrigatório")
        @Size(min = 4, message = "O username deve ter pelo menos 4 caracteres")
        String username,

        //@Schema(description = "Senha de acesso")
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String password,

        //@Schema(description = "Role do usuário")
        @NotNull(message = "O papel (role) é obrigatório")
        RolesUsuario role
) {}
