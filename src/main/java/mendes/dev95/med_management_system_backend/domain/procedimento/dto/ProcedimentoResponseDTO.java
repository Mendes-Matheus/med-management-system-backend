package mendes.dev95.med_management_system_backend.domain.procedimento.dto;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoSimpleResponseDTO;

import java.util.List;
import java.util.UUID;

public record ProcedimentoResponseDTO (
        UUID id,
        Long version,
        String nomeProcedimento,
        String orientacoes,
        String observacoes,
        List<EstabelecimentoSimpleResponseDTO> estabelecimentos
) {
}
