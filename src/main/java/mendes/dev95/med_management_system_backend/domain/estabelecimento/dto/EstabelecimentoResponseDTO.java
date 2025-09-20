package mendes.dev95.med_management_system_backend.domain.estabelecimento.dto;

import java.util.UUID;

public record EstabelecimentoResponseDTO(
    UUID id,
    Long version,
    String nomeEstabelecimento,
    String observacoes
) {
}
