package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import java.time.LocalDate;

public record PacienteSimpleResponseDTO(
        String nome,
        LocalDate dataNascimento,
        String cpf,
        String rg,
        String cns,
        String celular1,
        String celular2,
        String email,
        String sexo,
        String logradouro,
        String numero,
        String bairro,
        String observacoes
) {}
