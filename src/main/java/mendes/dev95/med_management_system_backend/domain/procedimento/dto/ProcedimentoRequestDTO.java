package mendes.dev95.med_management_system_backend.domain.procedimento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.TipoProcedimento;

import java.util.UUID;

public record ProcedimentoRequestDTO (

        @NotNull(message = "{procedimento.nomeProcedimento.notnull}")
        @NotBlank(message = "{procedimento.nomeProcedimento.notblank}")
        String nomeProcedimento,

        @Size(max = 1000, message = "{procedimento.orientacoes.size}")
        String orientacoes,

        @Size(max = 1000, message = "{procedimento.observacoes.size}")
        String observacoes,

        @NotNull(message = "Informe o tipo do procedimento")
        TipoProcedimento tipoProcedimento
){
}
