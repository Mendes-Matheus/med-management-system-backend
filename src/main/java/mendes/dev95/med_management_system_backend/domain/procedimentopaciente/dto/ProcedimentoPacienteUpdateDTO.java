package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;

import java.time.LocalDate;
import java.util.UUID;

public record ProcedimentoPacienteUpdateDTO(

        Boolean retorno,

        @NotNull(message = "{procedimento.paciente.request.dataSolicitacao.notnull}")
        LocalDate dataSolicitacao,

        LocalDate dataAgendamento,

        StatusProcedimento statusProcedimento,

        @Size(max = 1000, message = "{procedimento.paciente.request.observacoes.size}")
        String observacoes,

        @Size(max = 1000, message = "{procedimento.paciente.request.observacoes.size}")
        String orientacoes,

        @NotNull(message = "{procedimento.paciente.request.procedimentoId.notnull}")
        UUID procedimentoId
) {
}
