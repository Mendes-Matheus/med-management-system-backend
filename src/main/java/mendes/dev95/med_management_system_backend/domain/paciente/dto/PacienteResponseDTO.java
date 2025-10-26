package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteResponseDTO(
        UUID id,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        String rg,
        String cns,
        String nomePai,
        String nomeMae,
        String telefone,
        String telefoneSecundario,
        String email,
        String sexo,
        String logradouro,
        String numero,
        String bairro,
        String observacoes
) {
}
