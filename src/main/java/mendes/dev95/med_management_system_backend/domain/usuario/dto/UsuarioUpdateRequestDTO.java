package mendes.dev95.med_management_system_backend.domain.usuario.dto;

import jakarta.validation.constraints.*;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.RolesUsuario;

public record UsuarioUpdateRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Pattern(
                regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$",
                message = "O campo contém caracteres inválidos. Apenas letras são permitidas."
        )
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "O logradouro é obrigatório")
        @Pattern(
                regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
                message = "O campo contém caracteres inválidos."
        )
        String logradouro,

        @NotBlank(message = "O número é obrigatório")
        String numero,

        @Pattern(
                regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
                message = "O campo contém caracteres inválidos."
        )

        String complemento,

        @NotBlank(message = "A cidade é obrigatória")
        @Pattern(
                regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
                message = "O campo contém caracteres inválidos."
        )

        String cidade,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        String telefone,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "O e-mail deve ser válido"
        )
        String email,

        @NotBlank(message = "O username é obrigatório")
        @Size(min = 4, message = "O username deve ter pelo menos 4 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
                message = "O campo contém caracteres inválidos."
        )
        String username,

        @NotNull(message = "O papel (role) é obrigatório")
        RolesUsuario role
) {}
