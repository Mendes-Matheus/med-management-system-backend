package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;

import java.time.LocalDate;
import java.util.UUID;

public record ProcedimentoPacienteSimpleResponseDTO(
        UUID id,
        StatusProcedimento status,
        Boolean retorno,
        LocalDate dataSolicitacao,
        LocalDate dataAgendamento,
        PacienteSimpleResponseDTO paciente,
        ProcedimentoSimpleResponseDTO procedimento
) {}
