package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteResponseDTO(
        UUID id,
        Long version,
        String nome,
        LocalDate dataNascimento,
        String cpf,
        String rg,
        String cns,
        String nomePai,
        String nomeMae,
        String celular1,
        String celular2,
        String email,
        String sexo,
        String logradouro,
        String numero,
        String bairro,
        String observacoes
) {}
