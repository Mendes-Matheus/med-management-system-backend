package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record ProcedimentoPacienteRequestDTO(

        @NotNull(message = "{procedimento.paciente.request.dataSolicitacao.notnull}")
        LocalDate dataSolicitacao,

        @Size(max = 1000, message = "{procedimento.paciente.request.observacoes.size}")
        String observacoes,

        Boolean retorno,

        @NotNull(message = "{procedimento.paciente.request.pacienteId.notnull}")
        UUID pacienteId,

        @NotNull(message = "{procedimento.paciente.request.procedimentoId.notnull}")
        UUID procedimentoId

) {
}
