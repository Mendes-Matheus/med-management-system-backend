package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import java.time.LocalDate;

public record PacienteSimpleResponseDTO(
        String nome,
        String cpf,
        String rg,
        String cns,
        String telefone
) {}
