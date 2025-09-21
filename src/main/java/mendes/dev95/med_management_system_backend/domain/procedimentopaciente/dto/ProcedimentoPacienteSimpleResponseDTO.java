package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;

import java.time.LocalDate;

public record ProcedimentoPacienteSimpleResponseDTO(
    String status,
    LocalDate dataSolicitacao,
    LocalDate dataAgendamento,
    String observacoes,
    ProcedimentoSimpleResponseDTO procedimento
) {}
