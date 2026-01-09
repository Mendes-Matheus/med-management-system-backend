package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProcedimentoPendenteDTO(
        @JsonProperty("nome_procedimento")
        String nomeProcedimento,

        @JsonProperty("total_pendentes")
        Long totalPendentes
) {}
