package mendes.dev95.med_management_system_backend.estabelecimento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstabelecimentoRequestDTO(

        @NotBlank(message = "{estabelecimento.nomeEstabelecimento.NotBlank}")
        @Size(min = 3, max = 100, message = "{estabelecimento.nomeEstabelecimento.Size}")
        @NotNull(message = "{estabelecimento.nomeEstabelecimento.NotNull}")
        String nomeEstabelecimento,

        @Size(min = 3, max = 1000, message = "{estabelecimento.observacoes.Size}")
        String observacoes
) {
}
