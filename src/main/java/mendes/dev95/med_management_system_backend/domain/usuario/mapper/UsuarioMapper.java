package mendes.dev95.med_management_system_backend.domain.usuario.mapper;

import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioUpdateRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // converte DTO de registro em entidade
    Usuario toEntity(UsuarioRegisterRequestDTO dto);

    // converte entidade em resposta
    @Mapping(target = "authorities", ignore = true)
    UsuarioResponseDTO toResponse(Usuario entity);

    UsuarioResponseDTO toOptionalResponse(Optional<Usuario> entity);

    Usuario toEntityFromUpdate(UsuarioUpdateRequestDTO dto);

    // converte lista de entidades em lista de respostas
    List<UsuarioResponseDTO> toResponseList(List<Usuario> entities);

    // atualiza entidade existente a partir de um DTO (ignora id e version)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateEntityFromDto(UsuarioUpdateRequestDTO dto, @MappingTarget Usuario entity);

}
