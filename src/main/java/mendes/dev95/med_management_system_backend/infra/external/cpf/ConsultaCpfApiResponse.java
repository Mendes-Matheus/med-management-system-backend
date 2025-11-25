package mendes.dev95.med_management_system_backend.infra.external.cpf;

public record ConsultaCpfApiResponse(
    Integer code,
    DadosCpf data
) {}

