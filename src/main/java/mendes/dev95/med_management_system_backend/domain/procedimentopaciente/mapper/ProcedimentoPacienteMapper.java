package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteUpdateDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
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
                .dataSolicitacao(dto.dataSolicitacao())
                .observacoes(dto.observacoes())
                .paciente(pacienteId)
                .procedimento(procedimentoId)
                .build();

    }

    default ProcedimentoPacienteSimpleResponseDTO toResponse(ProcedimentoPaciente ep){
        var paciente = ep.getPaciente();
        var pacienteDTO = new PacienteSimpleResponseDTO(
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getRg(),
                paciente.getCns(),
                paciente.getTelefone()
        );

        var consultaDTO = getProcedimentoResponseDTO(ep);

        return new ProcedimentoPacienteSimpleResponseDTO(
                ep.getId(),
                ep.getStatus().name(),
                ep.getDataSolicitacao(),
                ep.getDataAgendamento(),
                pacienteDTO,
                consultaDTO
        );
    }

    private static ProcedimentoSimpleResponseDTO getProcedimentoResponseDTO(ProcedimentoPaciente ep){
        var procedimento = ep.getProcedimento();

        List<EstabelecimentoSimpleResponseDTO> estabelecimentosDTO = null;

        if (procedimento.getEstabelecimentos() != null && !procedimento.getEstabelecimentos().isEmpty()) {
            estabelecimentosDTO = procedimento.getEstabelecimentos().stream()
                    .map(estabelecimento -> new EstabelecimentoSimpleResponseDTO(
                            estabelecimento.getNomeEstabelecimento() // ou getNomeEstabelecimento() dependendo do seu modelo
                    ))
                    .collect(Collectors.toList());
        }

        return new ProcedimentoSimpleResponseDTO(
                procedimento.getId(),
                procedimento.getNomeProcedimento(),
                procedimento.getObservacoes(),
                procedimento.getOrientacoes(),
                estabelecimentosDTO
        );
    }

    default List<ProcedimentoPacienteSimpleResponseDTO> toResponseList(List<ProcedimentoPaciente> entities){
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "procedimento", ignore = true)
    void updateEntityFromDto(ProcedimentoPacienteUpdateDTO dto, @MappingTarget ProcedimentoPaciente entity);

}
