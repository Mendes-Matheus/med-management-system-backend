package mendes.dev95.med_management_system_backend.domain.paciente.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseWithProcedimentosDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteAlreadyExistsException;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.paciente.mapper.PacienteMapper;
import mendes.dev95.med_management_system_backend.domain.paciente.repository.PacienteRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository repository;
    private final MessageSource messageSource;
    private final PacienteMapper mapper;

    public PacienteResponseDTO save(PacienteRequestDTO dto) {
        var entity = mapper.toEntity(dto);
        validatePatient(entity);

        var saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    private void validatePatient(Paciente paciente) {
        if (
            repository.existsByCpf(paciente.getCpf()) ||
            repository.existsByRg(paciente.getRg()) ||
            repository.existsByCns(paciente.getCns()) ||
            repository.existsByEmail(paciente.getEmail())
        ) {
        throw new PacienteAlreadyExistsException();
            }
    }

    public List<PacienteResponseDTO> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public PacienteResponseDTO findById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
        return mapper.toResponse(entity);
    }

    public PacienteResponseDTO findByCpf(String cpf) {
        var entity = repository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException(cpf));
        return mapper.toResponse(entity);
    }

    public PacienteResponseDTO findByNome(String nome) {
        var entity = repository.findByNome(nome)
                .orElseThrow(() -> new PacienteNotFoundException(nome));
        return mapper.toResponse(entity);
    }

    public PacienteResponseWithProcedimentosDTO findProcedimentosPaciente(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
        return mapper.toDetailResponse(entity);
    }

    public PacienteResponseDTO update(UUID id, PacienteRequestDTO dto) {
        var paciente = repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));

        mapper.entityFromDto(dto, paciente);

        var updated = repository.save(paciente);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new PacienteNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
