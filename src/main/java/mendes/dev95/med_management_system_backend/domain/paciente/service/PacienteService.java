package mendes.dev95.med_management_system_backend.domain.paciente.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteRequestDTO;
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

    public Paciente save(Paciente paciente) {
        validatePatient(paciente);
        return repository.save(paciente);
    }

    private void validatePatient(Paciente paciente) {
        if (repository.existsByCpf(paciente.getCpf())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, getMessage("paciente.cpf.exist")
            );
        }
        if (repository.existsByRg(paciente.getRg())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, getMessage("paciente.rg.exist")
            );

        }
        if (repository.existsByCns(paciente.getCns())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, getMessage("paciente.cns.exist")
            );

        }
    }

    public List<Paciente> findAll() {
        return repository.findAll();
    }

    public Paciente findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
    }

    public Paciente findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
    }

    public Paciente findByNome(String nome) {
        return repository.findByNome(nome)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("paciente.notfound")));
    }

    public Paciente update(UUID id, PacienteRequestDTO dto) {
        Paciente paciente = findById(id);
        mapper.entityFromDto(dto, paciente);
        return repository.save(paciente);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getMessage("paciente.delete.notfound"));
        }
        repository.deleteById(id);
    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}