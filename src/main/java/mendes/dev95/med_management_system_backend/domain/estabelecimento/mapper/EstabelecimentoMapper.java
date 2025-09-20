package mendes.dev95.med_management_system_backend.domain.estabelecimento.mapper;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.entity.Estabelecimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstabelecimentoMapper {

    Estabelecimento toEntity(EstabelecimentoRequestDTO dto);

    EstabelecimentoResponseDTO toResponse(Estabelecimento entity);

    List<EstabelecimentoResponseDTO> toResponseList(List<Estabelecimento> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(EstabelecimentoRequestDTO dto, @MappingTarget Estabelecimento entity);

}
