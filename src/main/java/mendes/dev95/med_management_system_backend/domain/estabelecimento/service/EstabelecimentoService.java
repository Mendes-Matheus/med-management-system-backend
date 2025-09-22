package mendes.dev95.med_management_system_backend.domain.estabelecimento.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.mapper.EstabelecimentoMapper;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.repository.EstabelecimentoRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final MessageSource messageSource;
    private final EstabelecimentoMapper mapper;

    public EstabelecimentoResponseDTO save(EstabelecimentoRequestDTO dto) {
        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public List<EstabelecimentoResponseDTO> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public EstabelecimentoResponseDTO findById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("estabelecimento.notfound")
                ));
        return mapper.toResponse(entity);
    }

    public EstabelecimentoResponseDTO update(UUID id, EstabelecimentoRequestDTO dto) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("estabelecimento.notfound")
                ));

        mapper.updateEntityFromDto(dto, entity);

        var updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    getMessage("estabelecimento.delete.notfound")
            );
        }
        repository.deleteById(id);
    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
