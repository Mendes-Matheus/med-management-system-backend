package mendes.dev95.med_management_system_backend.domain.procedimento.service;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.repository.EstabelecimentoRepository;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.procedimento.mapper.ProcedimentoMapper;
import mendes.dev95.med_management_system_backend.domain.procedimento.repository.ProcedimentoRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
@Log4j2
@RequiredArgsConstructor
public class ProcedimentoService {

    private final ProcedimentoRepository repository;
    private final MessageSource messageSource;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ProcedimentoMapper mapper;

    public ProcedimentoResponseDTO save(ProcedimentoRequestDTO dto) {
        var entity = mapper.toEntity(dto);

        var estabelecimento = estabelecimentoRepository.findById(entity.getEstabelecimento().getId())
                .orElseThrow(() -> new EstabelecimentoNotFoundException(entity.getEstabelecimento().getId()));

        var procedimentoToSave = Procedimento.builder()
                .id(entity.getId())
                .estabelecimento(estabelecimento)
                .nomeProcedimento(entity.getNomeProcedimento())
                .tipoProcedimento(entity.getTipoProcedimento())
                .observacoes(entity.getObservacoes())
                .orientacoes(entity.getOrientacoes())
                .build();

        var saved = repository.save(procedimentoToSave);
        return mapper.toResponse(saved);
    }

    public List<ProcedimentoResponseDTO> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public ProcedimentoResponseDTO findById(UUID id) {
        var procedimento = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));
        return mapper.toResponse(procedimento);
    }

    public ProcedimentoResponseDTO update(UUID id, ProcedimentoRequestDTO dto) {
        var procedimento = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));

        mapper.updateEntityFromDto(dto, procedimento);

        if (dto.estabelecimentoId() == null) {
            var estabelecimento = estabelecimentoRepository.findById(procedimento.getEstabelecimento().getId())
                    .orElseThrow(() -> new EstabelecimentoNotFoundException(procedimento.getEstabelecimento().getId()));
            procedimento.setEstabelecimento(estabelecimento);
        }

        var updated = repository.save(procedimento);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProcedimentoNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private String getMessage(@NonNull String code, @NonNull Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}

