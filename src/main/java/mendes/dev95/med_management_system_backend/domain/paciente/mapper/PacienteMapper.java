package mendes.dev95.med_management_system_backend.domain.paciente.mapper;

import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseWithProcedimentosDTO;
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

    @Mapping(target = "procedimentos", ignore = true)
    PacienteResponseDTO toResponse(Paciente entity);

    PacienteResponseWithProcedimentosDTO toDetailResponse(Paciente entity);

    List<PacienteResponseDTO> toResponseList(List<Paciente> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "procedimentos", ignore = true)
    void entityFromDto(PacienteRequestDTO dto, @MappingTarget Paciente entity);
}

