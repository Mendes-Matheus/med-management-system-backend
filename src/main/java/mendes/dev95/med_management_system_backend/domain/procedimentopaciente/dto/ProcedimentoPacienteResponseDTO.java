package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;

import java.time.LocalDate;
import java.util.UUID;

public record ProcedimentoPacienteResponseDTO (

        UUID id,
        Long version,
        String status,
        LocalDate dataSolicitacao,
        LocalDate dataAgendamento,
        String observacoes,
        PacienteResponseDTO paciente,
        ProcedimentoResponseDTO procedimento
) {
}
