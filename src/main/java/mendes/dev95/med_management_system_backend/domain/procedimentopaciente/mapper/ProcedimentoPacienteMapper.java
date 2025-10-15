package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProcedimentoPacienteMapper {

    default ProcedimentoPaciente toEntity(ProcedimentoPacienteRequestDTO dto){
        var pacienteId = new Paciente();
        pacienteId.setId(dto.pacienteId());

        var procedimentoId = new Procedimento();
        procedimentoId.setId(dto.procedimentoId());

        return ProcedimentoPaciente.builder()
                .status(StatusProcedimento.valueOf(dto.status()))
                .dataSolicitacao(dto.dataSolicitacao())
                .dataAgendamento(dto.dataAgendamento())
                .observacoes(dto.observacoes())
                .paciente(pacienteId)
                .procedimento(procedimentoId)
                .build();

    }

    default ProcedimentoPacienteResponseDTO toResponse(ProcedimentoPaciente ep){
        var paciente = ep.getPaciente();
        var pacienteDTO = new PacienteResponseDTO(
                paciente.getId(),
                paciente.getVersion(),
                paciente.getNome(),
                paciente.getDataNascimento(),
                paciente.getCpf(),
                paciente.getRg(),
                paciente.getCns(),
                paciente.getNomePai(),
                paciente.getNomeMae(),
                paciente.getTelefone(),
                paciente.getTelefoneSecundario(),
                paciente.getSexo(),
                paciente.getLogradouro(),
                paciente.getNumero(),
                paciente.getBairro(),
                paciente.getObservacoes(),
                paciente.getEmail()
        );

        var consultaDTO = getProcedimentoResponseDTO(ep);

        return new ProcedimentoPacienteResponseDTO(
                ep.getId(),
                ep.getVersion(),
                ep.getStatus().name(),
                ep.getDataSolicitacao(),
                ep.getDataAgendamento(),
                ep.getObservacoes(),
                pacienteDTO,
                consultaDTO
        );
    }

    private static ProcedimentoResponseDTO getProcedimentoResponseDTO(ProcedimentoPaciente ep){
        var procedimento = ep.getProcedimento();

        // CORREÇÃO: Agora trabalhamos com lista de estabelecimentos
        List<EstabelecimentoSimpleResponseDTO> estabelecimentosDTO = null;

        if (procedimento.getEstabelecimentos() != null && !procedimento.getEstabelecimentos().isEmpty()) {
            estabelecimentosDTO = procedimento.getEstabelecimentos().stream()
                    .map(estabelecimento -> new EstabelecimentoSimpleResponseDTO(
                            estabelecimento.getNomeEstabelecimento() // ou getNomeEstabelecimento() dependendo do seu modelo
                    ))
                    .collect(Collectors.toList());
        }

        return new ProcedimentoResponseDTO(
                procedimento.getId(),
                procedimento.getVersion(),
                procedimento.getNomeProcedimento(),
                procedimento.getTipoProcedimento(),
                procedimento.getObservacoes(),
                procedimento.getOrientacoes(),
                estabelecimentosDTO
        );
    }
    default List<ProcedimentoPacienteResponseDTO> toResponseList(List<ProcedimentoPaciente> entities){
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "procedimento", ignore = true)
    void updateEntityFromDto(ProcedimentoPacienteRequestDTO dto, @MappingTarget ProcedimentoPaciente entity);

}
