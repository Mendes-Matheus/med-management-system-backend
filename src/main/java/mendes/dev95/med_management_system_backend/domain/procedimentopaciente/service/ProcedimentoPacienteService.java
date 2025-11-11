package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.paciente.repository.PacienteRepository;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoAgendadoException;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.procedimento.repository.ProcedimentoRepository;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteUpdateDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.exception.ProcedimentoPacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper.ProcedimentoPacienteMapper;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository.ProcedimentoPacienteRepository;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioFetchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProcedimentoPacienteService {

    private final ProcedimentoPacienteRepository repository;
    private final ProcedimentoRepository procedimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimentoPacienteMapper mapper;
    private final MessageSource messageSource;

    public ProcedimentoPacienteSimpleResponseDTO save(ProcedimentoPacienteRequestDTO dto) {
        var entity = mapper.toEntity(dto);

        var paciente = pacienteRepository.findById(entity.getPaciente().getId())
                .orElseThrow(() -> new PacienteNotFoundException(entity.getPaciente().getId()));

        var procedimento = procedimentoRepository.findById(entity.getProcedimento().getId())
                .orElseThrow(() -> new ProcedimentoNotFoundException(entity.getProcedimento().getId()));

        var verificarStatus = List.of(
                StatusProcedimento.PENDENTE,
                StatusProcedimento.AGENDADA,
                StatusProcedimento.REAGENDADA,
                StatusProcedimento.URGENTE,
                StatusProcedimento.IMEDIATA
        );

        boolean jaAgendado = repository.existsByPacienteIdAndProcedimentoIdAndStatusIn(
                paciente.getId(),
                procedimento.getId(),
                verificarStatus
        );

        if (jaAgendado) {
            throw new ProcedimentoAgendadoException();
        }

        entity.setStatus(StatusProcedimento.PENDENTE);

        var procedimentoPacienteToSave = ProcedimentoPaciente.builder()
                .status(entity.getStatus())
                .dataSolicitacao(entity.getDataSolicitacao())
                .dataAgendamento(entity.getDataAgendamento())
                .paciente(paciente)
                .procedimento(procedimento)
                .build();

        var saved = repository.save(procedimentoPacienteToSave);
        return mapper.toSimpleResponse(saved);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findAll(Pageable pageable) {
        return repository.findAllProcedimentos(pageable);
    }


    public ProcedimentoPacienteResponseDTO findById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));
        return mapper.toFullResponse(entity);
    }

    public ProcedimentoPacienteSimpleResponseDTO findByPacienteId(UUID pacienteId) {
        var entity = repository.findByPacienteId(pacienteId);
        return mapper.toSimpleResponse(entity);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findByProcedimentoStatus(String status, Pageable pageable) {
        try {
            StatusProcedimento statusEnum = StatusProcedimento.valueOf(status.toUpperCase());
            return repository.findByProcedimentoStatus(statusEnum, pageable);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByProcedimentoStatus(String status, Pageable pageable) {
        try {
            StatusProcedimento statusEnum = StatusProcedimento.valueOf(status.toUpperCase());
            return repository.findConsultasByProcedimentoStatus(statusEnum, pageable);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByProcedimentoStatus(String status, Pageable pageable) {
        try {
            StatusProcedimento statusEnum = StatusProcedimento.valueOf(status.toUpperCase());
            return repository.findExamesByProcedimentoStatus(statusEnum, pageable);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByProcedimentoStatus(String status, Pageable pageable) {
        try {
            StatusProcedimento statusEnum = StatusProcedimento.valueOf(status.toUpperCase());
            return repository.findCirurgiasByProcedimentoStatus(statusEnum, pageable);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findByProcedimentoId(UUID procedimentoId, Pageable pageable) {
        return repository.findByProcedimentoId(procedimentoId, pageable);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByProcedimentoId(UUID procedimentoId, Pageable pageable) {
        return repository.findConsultasByProcedimentoId(procedimentoId, pageable);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByProcedimentoId(UUID procedimentoId, Pageable pageable) {
        return repository.findExamesByProcedimentoId(procedimentoId, pageable);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByProcedimentoId(UUID procedimentoId, Pageable pageable) {
        return repository.findCirurgiasByProcedimentoId(procedimentoId, pageable);
    }




    public Page<ProcedimentoPacienteSimpleResponseDTO> findByPacienteCpf(String cpf, Pageable pageable) {
        log.debug("Fetching procedimentos for paciente with CPF {}", cpf);
        // valida existência do paciente antes de buscar
        pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException("Paciente com CPF " + cpf + " não encontrado."));

        try {
            var procedimentos = repository.findByPacienteCpf(cpf, pageable);
            log.debug("Found {} procedimentos for CPF {}", procedimentos.getTotalElements(), cpf);
            return procedimentos;
        } catch (Exception ex) {
            log.error("Error fetching procedimentos for CPF {}", cpf, ex);
            throw new ProcedimentoPacienteNotFoundException("Erro ao buscar procedimentos do paciente com CPF " + cpf, ex);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByPacienteCpf(String cpf, Pageable pageable) {
        log.debug("Fetching procedimentos for paciente with CPF {}", cpf);
        pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException("Paciente com CPF " + cpf + " não encontrado."));

        try {
            var procedimentos = repository.findConsultasByPacienteCpf(cpf, pageable);
            log.debug("Found {} procedimentos for CPF {}", procedimentos.getTotalElements(), cpf);
            return procedimentos;
        } catch (Exception ex) {
            log.error("Error fetching procedimentos for CPF {}", cpf, ex);
            throw new ProcedimentoPacienteNotFoundException("Erro ao buscar procedimentos do paciente com CPF " + cpf, ex);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByPacienteCpf(String cpf, Pageable pageable) {
        log.debug("Fetching procedimentos for paciente with CPF {}", cpf);
        pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException("Paciente com CPF " + cpf + " não encontrado."));

        try {
            var procedimentos = repository.findExamesByPacienteCpf(cpf, pageable);
            log.debug("Found {} procedimentos for CPF {}", procedimentos.getTotalElements(), cpf);
            return procedimentos;
        } catch (Exception ex) {
            log.error("Error fetching procedimentos for CPF {}", cpf, ex);
            throw new ProcedimentoPacienteNotFoundException("Erro ao buscar procedimentos do paciente com CPF " + cpf, ex);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByPacienteCpf(String cpf, Pageable pageable) {
        log.debug("Fetching procedimentos for paciente with CPF {}", cpf);
        pacienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new PacienteNotFoundException("Paciente com CPF " + cpf + " não encontrado."));

        try {
            var procedimentos = repository.findCirurgiasByPacienteCpf(cpf, pageable);
            log.debug("Found {} procedimentos for CPF {}", procedimentos.getTotalElements(), cpf);
            return procedimentos;
        } catch (Exception ex) {
            log.error("Error fetching procedimentos for CPF {}", cpf, ex);
            throw new ProcedimentoPacienteNotFoundException("Erro ao buscar procedimentos do paciente com CPF " + cpf, ex);
        }
    }

    public ProcedimentoPacienteSimpleResponseDTO update(UUID id, ProcedimentoPacienteUpdateDTO dto) {
        var procedimentoPaciente = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));

        mapper.updateEntityFromDto(dto, procedimentoPaciente);

        if (dto.procedimentoId() != null) {
            var procedimento = procedimentoRepository.findById(dto.procedimentoId())
                    .orElseThrow(() -> new ProcedimentoNotFoundException(dto.procedimentoId()));
            procedimentoPaciente.setProcedimento(procedimento);
        }

        if (dto.dataSolicitacao() != null) {
            procedimentoPaciente.setDataSolicitacao(dto.dataSolicitacao());
        }

        if (dto.dataAgendamento() != null) {
            procedimentoPaciente.setDataAgendamento(dto.dataAgendamento());
        }

        if (dto.statusProcedimento() != null) {
            procedimentoPaciente.setStatus(dto.statusProcedimento());
        }

        if (dto.observacoes() != null) {
            procedimentoPaciente.setObservacoes(dto.observacoes());
        }

        var updated = repository.save(procedimentoPaciente);
        return mapper.toSimpleResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProcedimentoNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findAllConsultas(Pageable pageable) {
        try {
            var consultas = repository.findAllConsultas(pageable);
            return consultas;
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw new UsuarioFetchException(getMessage("procedimentopaciente.fetch.error"), ex);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findAllExames(Pageable pageable) {
        try {
            var exames = repository.findAllExames(pageable);
            return exames;
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw new UsuarioFetchException(getMessage("procedimentopaciente.fetch.error"), ex);
        }
    }

    public Page<ProcedimentoPacienteSimpleResponseDTO> findAllCirurgias(Pageable pageable) {
        try {
            var cirurgias = repository.findAllCirurgias(pageable);
            return cirurgias;
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw new UsuarioFetchException(getMessage("procedimentopaciente.fetch.error"), ex);
        }
    }



    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }


}

