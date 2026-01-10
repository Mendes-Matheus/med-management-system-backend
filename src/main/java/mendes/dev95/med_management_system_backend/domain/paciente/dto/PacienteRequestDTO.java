package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PacienteRequestDTO(

        @NotBlank(message = "{paciente.nome.notblank}")
        @NotNull(message = "{paciente.nome.notnull}")
        @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$",
            message = "O campo contém caracteres inválidos. Apenas letras são permitidas."
        )
        @Size(min = 2, max = 100, message = "{paciente.nome.size}")
        String nome,

        @NotNull(message = "{paciente.dataNascimento.notnull}")
        @PastOrPresent(message = "{paciente.dataNascimento.dataNascimento.pastOrPresent}")
        LocalDate dataNascimento,

        @NotNull(message = "{paciente.cpf.notnull}")
        @Size(min = 11, max = 11, message = "{paciente.cpf.size}")
        @NotBlank(message = "{paciente.cpf.notblank}")
        String cpf,

        @NotNull(message = "{paciente.rg.notnull}")
        @Size(min = 9, max = 10, message = "{paciente.rg.size}")
        @NotBlank(message = "{paciente.rg.notblank}")
        String rg,

        @NotNull(message = "{paciente.cns.notnull}")
        @Size(min = 15, max = 15, message = "{paciente.cns.size}")
        @NotBlank(message = "{paciente.cns.notblank}")
        String cns,

        @Size(max = 100, message = "{paciente.nomeMae.size}")
        @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$",
            message = "O campo contém caracteres inválidos. Apenas letras são permitidas."
        )
        String nomePai,

        @Size(max = 100, message = "{paciente.nomeMae.size}")
        @NotBlank(message = "{paciente.nomeMae.notblank}")
        @NotNull
        @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$",
            message = "O campo contém caracteres inválidos. Apenas letras são permitidas."
        )
        String nomeMae,

        @NotNull(message = "{paciente.celular.notnull}")
        @Size(min = 10, max = 11, message = "{paciente.celular.size}")
        @NotBlank(message = "{paciente.celular.notblank}")
        String telefone,

        @Size(min = 10, max = 11, message = "{paciente.celular.size}")
        String telefoneSecundario,

        @Size(max = 100, message = "{paciente.sexo.size}")
        @NotBlank(message = "{paciente.sexo.notblank}")
        @NotNull(message = "{paciente.sexo.notnull}")
        String sexo,

        @Size(max = 100, message = "{paciente.logradouro.size}")
        @NotBlank(message = "{paciente.logradouro.notblank}")
        @NotNull(message = "{paciente.logradouro.notnull}")
        @Pattern(
            regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
            message = "O campo contém caracteres inválidos."
        )
        String logradouro,

        @Size(max = 100, message = "{paciente.numero.size}")
        @NotBlank(message = "{paciente.numero.notblank}")
        @NotNull(message = "{paciente.numero.notnull}")
        String numero,

        @Size(max = 100, message = "{paciente.bairro.size}")
        @NotBlank(message = "{paciente.bairro.notblank}")
        @NotNull(message = "{paciente.bairro.notnull}")
        @Pattern(
            regexp = "^[a-zA-Z0-9À-ÿ\\s]+$",
            message = "O campo contém caracteres inválidos."
        )
        String bairro,

        @Size(max = 1000, message = "{paciente.observacoes.size}")
        @Pattern(
            regexp = "^[a-zA-Z0-9À-ÿ.,!?\\s]+$",
            message = "O campo contém caracteres inválidos."
        )
        String observacoes,

        @Email(message = "{paciente.email.email}")
        String email
) {}
