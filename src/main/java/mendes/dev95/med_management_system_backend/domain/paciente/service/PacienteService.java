package mendes.dev95.med_management_system_backend.domain.paciente.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseWithProcedimentosDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.paciente.mapper.PacienteMapper;
import mendes.dev95.med_management_system_backend.domain.paciente.repository.PacienteRepository;
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
        if (repository.existsByCpf(paciente.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, getMessage("paciente.cpf.exist"));
        }
        if (repository.existsByRg(paciente.getRg())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, getMessage("paciente.rg.exist"));
        }
        if (repository.existsByCns(paciente.getCns())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, getMessage("paciente.cns.exist"));
        }
        if (repository.existsByEmail(paciente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, getMessage("paciente.email.exist"));
        }
    }

    public List<PacienteResponseDTO> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public PacienteResponseDTO findById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
        return mapper.toResponse(entity);
    }

    public PacienteResponseDTO findByCpf(String cpf) {
        var entity = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
        return mapper.toResponse(entity);
    }

    public PacienteResponseDTO findByNome(String nome) {
        var entity = repository.findByNome(nome)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
        return mapper.toResponse(entity);
    }

    public PacienteResponseWithProcedimentosDTO findProcedimentosPaciente(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
        return mapper.toDetailResponse(entity);
    }

    public PacienteResponseDTO update(UUID id, PacienteRequestDTO dto) {
        var paciente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));

        mapper.entityFromDto(dto, paciente);

        var updated = repository.save(paciente);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, getMessage("paciente.delete.notfound"));
        }
        repository.deleteById(id);
    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
