package mendes.dev95.med_management_system_backend.domain.procedimento.mapper;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.entity.Estabelecimento;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.mapper.EstabelecimentoMapper;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EstabelecimentoMapper.class})
public interface ProcedimentoMapper {

    default Procedimento toEntity(ProcedimentoRequestDTO dto) {
        var estabelecimento = new Estabelecimento();
        estabelecimento.setId(dto.estabelecimentoId());

        return Procedimento.builder()
                .nomeProcedimento(dto.nomeProcedimento())
                .observacoes(dto.observacoes())
                .estabelecimento(estabelecimento) // agora não é null
                .build();
    }

    ProcedimentoResponseDTO toResponse(Procedimento entity);

    List<ProcedimentoResponseDTO> toResponseList(List<Procedimento> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "estabelecimento", ignore = true)
    void updateEntityFromDto(ProcedimentoRequestDTO dto, @MappingTarget Procedimento entity);

}
