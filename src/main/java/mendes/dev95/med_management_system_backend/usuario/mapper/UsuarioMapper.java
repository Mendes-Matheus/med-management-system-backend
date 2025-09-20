package mendes.dev95.med_management_system_backend.usuario.mapper;

import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.usuario.entity.Usuario;
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
    UsuarioResponseDTO toResponse(Usuario entity);

    UsuarioResponseDTO toOptionalResponse(Optional<Usuario> entity);


    // converte lista de entidades em lista de respostas
    List<UsuarioResponseDTO> toResponseList(List<Usuario> entities);

    // atualiza entidade existente a partir de um DTO (ignora id e version)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(UsuarioRegisterRequestDTO dto, @MappingTarget Usuario entity);
}
