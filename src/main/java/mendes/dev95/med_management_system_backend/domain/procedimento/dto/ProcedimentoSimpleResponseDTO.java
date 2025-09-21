package mendes.dev95.med_management_system_backend.domain.procedimento.dto;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoSimpleResponseDTO;

public record ProcedimentoSimpleResponseDTO(
    String nomeProcedimento,
    String orientacoes,
    String observacoes,
    EstabelecimentoSimpleResponseDTO estabelecimento
) {}
