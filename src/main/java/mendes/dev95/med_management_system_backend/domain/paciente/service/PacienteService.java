package mendes.dev95.med_management_system_backend.domain.paciente.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.*;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteAlreadyExistsException;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.paciente.mapper.PacienteMapper;
import mendes.dev95.med_management_system_backend.domain.paciente.repository.PacienteRepository;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioFetchException;
import mendes.dev95.med_management_system_backend.infra.external.cpf.CpfApiClient;
import mendes.dev95.med_management_system_backend.infra.external.cpf.CpfApiException;
import mendes.dev95.med_management_system_backend.infra.external.cpf.CpfApiResponse;
import mendes.dev95.med_management_system_backend.infra.util.MaskUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {

    private final PacienteRepository repository;
    private final MessageSource messageSource;
    private final PacienteMapper mapper;
    private final CpfApiClient cpfApiClient;

    @Transactional
    public PacienteFullResponseDTO save(PacienteRequestDTO dto) {
        log.debug("Tentando salvar paciente com CPF: {}", MaskUtil.maskCpf(dto.cpf()));

        ensureUniquePaciente(dto.cpf(), dto.rg(), dto.cns(), dto.email());

        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);

        log.info("Paciente salvo com sucesso: {}", MaskUtil.maskCpf(saved.getCpf()));
        return mapper.toFullResponse(saved);
    }

    private void ensureUniquePaciente(String cpf, String rg, String cns, String email) {
        if (repository.findByCpf(cpf).isPresent()) {
            log.warn("Tentativa de registro com CPF já existente: {}", MaskUtil.maskCpf(cpf));
            throw new PacienteAlreadyExistsException("paciente.cpf.alreadyexists");
        }

        if (repository.findByRg(rg).isPresent()) {
            log.warn("Tentativa de registro com RG já existente: {}", rg);
            throw new PacienteAlreadyExistsException("paciente.rg.alreadyexists");
        }

        if (repository.findByCns(cns).isPresent()) {
            log.warn("Tentativa de registro com CNS já existente: {}", cns);
            throw new PacienteAlreadyExistsException("paciente.cns.alreadyexists");
        }

        if (repository.findByEmail(email).isPresent()) {
            log.warn("Tentativa de registro com e-mail já existente: {}", MaskUtil.maskEmail(email));
            throw new PacienteAlreadyExistsException("paciente.email.alreadyexists");
        }
    }

    public Page<PacienteResponseDTO> findAll(Pageable pageable) {
        try {
            var pacientes = repository.findAllPacientes(pageable);
            return pacientes;
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw new UsuarioFetchException(getMessage("usuario.fetch.error"), ex);
        }
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

    public PacienteResponseDTO findByCns(String cns) {
        var entity = repository.findByCns(cns)
                .orElseThrow(() -> new PacienteNotFoundException(cns));
        return mapper.toResponse(entity);
    }

    public Page<PacienteResponseDTO> findByNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public PacienteResponseWithProcedimentosDTO findProcedimentosPaciente(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));
        return mapper.toDetailResponse(entity);
    }

    @Transactional
    public PacienteResponseDTO update(UUID id, PacienteUpdateRequestDTO dto) {
        var paciente = repository.findById(id)
                .orElseThrow(() -> new PacienteNotFoundException(id));

        mapper.entityFromUpdateDto(dto, paciente);
        var updated = repository.save(paciente);

        log.info("Paciente atualizado com sucesso: {}", MaskUtil.maskCpf(updated.getCpf()));
        return mapper.toResponse(updated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new PacienteNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("Paciente removido com sucesso: {}", id);
    }

//    public CpfApiResponse consultarCpf(String cpf) {
//        log.debug("Iniciando consulta de CPF: {}", MaskUtil.maskCpf(cpf));
//
//        // Validação básica do CPF
//        if (cpf == null || cpf.trim().isEmpty() || cpf.length() != 11) {
//            throw new IllegalArgumentException("CPF inválido");
//        }
//
//        try {
//            CpfApiResponse response = cpfApiClient.consultarCpf(cpf);
//
//            if (response.code() != 200 || response.data() == null) {
//                log.warn("API de CPF retornou código não sucedido para {}: {}",
//                        MaskUtil.maskCpf(cpf), response.code());
//                throw new CpfApiException("CPF não encontrado ou inválido");
//            }
//
//            log.debug("Consulta de CPF realizada com sucesso para: {}", MaskUtil.maskCpf(cpf));
//            return response;
//
//        } catch (CpfApiException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("Erro inesperado ao consultar CPF {}: {}", MaskUtil.maskCpf(cpf), e.getMessage());
//            throw new CpfApiException("Erro ao consultar CPF: " + e.getMessage());
//        }
//    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
