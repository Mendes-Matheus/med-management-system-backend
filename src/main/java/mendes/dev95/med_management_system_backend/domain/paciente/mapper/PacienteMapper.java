package mendes.dev95.med_management_system_backend.domain.paciente.mapper;

import mendes.dev95.med_management_system_backend.domain.paciente.dto.*;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper.ProcedimentoPacienteMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProcedimentoPacienteMapper.class})
public interface PacienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "procedimentos", ignore = true)
    Paciente toEntity(PacienteRequestDTO dto);

    PacienteFullResponseDTO toFullResponse(Paciente entity);

    PacienteResponseDTO toResponse(Paciente entity);

    @Mapping(target = "procedimentos", ignore = true)
    PacienteResponseWithProcedimentosDTO toDetailResponse(Paciente entity);

    List<PacienteResponseDTO> toResponseList(List<Paciente> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void entityFromDto(PacienteRequestDTO dto, @MappingTarget Paciente entity);


    void entityFromUpdateDto(PacienteUpdateRequestDTO dto, @MappingTarget Paciente entity);
}

