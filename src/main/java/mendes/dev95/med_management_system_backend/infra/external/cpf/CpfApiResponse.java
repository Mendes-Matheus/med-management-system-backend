package mendes.dev95.med_management_system_backend.infra.external.cpf;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CpfApiResponse(
        Integer code,
        CpfData data
) {
    public record CpfData(
            @JsonProperty("cpf")
            String cpf,

            @JsonProperty("nome")
            String nome,

            @JsonProperty("genero")
            String genero,

            @JsonProperty("data_nascimento")
            String dataNascimento
    ) {}
}