package mendes.dev95.med_management_system_backend.domain.estabelecimento.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoIntegrityViolationException;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.mapper.EstabelecimentoMapper;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.repository.EstabelecimentoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final EstabelecimentoMapper mapper;

    @Transactional
    public EstabelecimentoResponseDTO save(EstabelecimentoRequestDTO dto) {
        log.info("Saving new estabelecimento");

        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);
        var response = mapper.toResponse(saved);

        log.info("Estabelecimento saved successfully with ID: {}", response.id());
        return response;
    }

    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findAll() {
        log.info("Retrieving all estabelecimentos");

        var entities = repository.findAll();
        var responses = mapper.toResponseList(entities);

        log.info("Retrieved {} estabelecimentos", responses.size());
        return responses;
    }

    @Transactional(readOnly = true)
    public EstabelecimentoResponseDTO findById(UUID id) {
        log.info("Searching for estabelecimento with ID: {}", id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new EstabelecimentoNotFoundException(id));

        var response = mapper.toResponse(entity);
        log.info("Estabelecimento found with ID: {}", id);
        return response;
    }

    @Transactional
    public EstabelecimentoResponseDTO update(UUID id, EstabelecimentoRequestDTO dto) {
        log.info("Updating estabelecimento with ID: {}", id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new EstabelecimentoNotFoundException(id));

        mapper.updateEntityFromDto(dto, entity);
        var updated = repository.save(entity);
        var response = mapper.toResponse(updated);

        log.info("Estabelecimento updated successfully with ID: {}", id);
        return response;
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Deleting estabelecimento with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new EstabelecimentoNotFoundException(id);
        }

        try {
            repository.deleteById(id);
            log.info("Estabelecimento deleted successfully with ID: {}", id);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Cannot delete estabelecimento due to integrity constraints. ID: {}", id);
            throw new EstabelecimentoIntegrityViolationException(id, "delete");
        }
    }
}
