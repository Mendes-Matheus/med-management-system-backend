package mendes.dev95.med_management_system_backend.domain.procedimento.service;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.repository.EstabelecimentoRepository;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.service.EstabelecimentoService;
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
import org.springframework.transaction.annotation.Transactional;
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
    private final EstabelecimentoService estabelecimentoService;

    @Transactional
    public ProcedimentoResponseDTO save(ProcedimentoRequestDTO dto) {
        var entity = mapper.toEntity(dto);

        var procedimentoToSave = Procedimento.builder()
                .id(entity.getId())
                .nomeProcedimento(entity.getNomeProcedimento())
                .tipoProcedimento(entity.getTipoProcedimento())
                .observacoes(entity.getObservacoes())
                .orientacoes(entity.getOrientacoes())
                .build();

        var saved = repository.save(procedimentoToSave);
        return mapper.toResponse(saved);
    }

    @Transactional
    public ProcedimentoResponseDTO associarEstabelecimento(UUID procedimentoId, UUID estabelecimentoId) {
        var procedimento = repository.findById(procedimentoId)
                .orElseThrow(() -> new ProcedimentoNotFoundException(procedimentoId));

        var estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new EstabelecimentoNotFoundException(estabelecimentoId));

        // Verifica se o estabelecimento já está associado
        boolean jaAssociado = procedimento.getEstabelecimentos().stream()
                .anyMatch(est -> est.getId().equals(estabelecimentoId));

        if (!jaAssociado) {
            procedimento.getEstabelecimentos().add(estabelecimento);
            var updated = repository.save(procedimento);
            log.info("Estabelecimento {} associado ao procedimento {}", estabelecimentoId, procedimentoId);
            return mapper.toResponse(updated);
        } else {
            log.info("Estabelecimento {} já está associado ao procedimento {}", estabelecimentoId, procedimentoId);
            return mapper.toResponse(procedimento);
        }
    }

    @Transactional
    public ProcedimentoResponseDTO removerEstabelecimento(UUID procedimentoId, UUID estabelecimentoId) {
        var procedimento = repository.findById(procedimentoId)
                .orElseThrow(() -> new ProcedimentoNotFoundException(procedimentoId));

        boolean removed = procedimento.getEstabelecimentos()
                .removeIf(est -> est.getId().equals(estabelecimentoId));

        if (removed) {
            var updated = repository.save(procedimento);
            log.info("Estabelecimento {} removido do procedimento {}", estabelecimentoId, procedimentoId);
            return mapper.toResponse(updated);
        } else {
            log.warn("Estabelecimento {} não encontrado no procedimento {}", estabelecimentoId, procedimentoId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Estabelecimento não associado a este procedimento");
        }
    }

    public List<ProcedimentoResponseDTO> findByEstabelecimentoId(UUID estabelecimentoId) {
        var estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new EstabelecimentoNotFoundException(estabelecimentoId));

        var procedimentos = repository.findByEstabelecimentosContaining(estabelecimento);
        return mapper.toResponseList(procedimentos);
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

