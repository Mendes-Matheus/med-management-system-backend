package mendes.dev95.med_management_system_backend.infra.external.cpf;

public record DadosCpf(
    String cpf,
    String nome,
    String genero,
    String data_nascimento
) {}

