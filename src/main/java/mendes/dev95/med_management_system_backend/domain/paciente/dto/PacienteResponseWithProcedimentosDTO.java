package mendes.dev95.med_management_system_backend.domain.paciente.dto;

import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;

import java.time.LocalDate;
import java.util.List;

public record PacienteResponseWithProcedimentosDTO(
        String nome,
        LocalDate dataNascimento,
        String cpf,
        String rg,
        String cns,
        String telefone,
        String telefoneSecundario,
        String sexo,
        String logradouro,
        String numero,
        String bairro,
        String observacoes,
        List<ProcedimentoPacienteSimpleResponseDTO> procedimentos
) {}
