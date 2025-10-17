package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.paciente.repository.PacienteRepository;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoAgendadoException;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.procedimento.repository.ProcedimentoRepository;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper.ProcedimentoPacienteMapper;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository.ProcedimentoPacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProcedimentoPacienteService {

    private final ProcedimentoPacienteRepository repository;
    private final ProcedimentoRepository procedimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimentoPacienteMapper mapper;

    public ProcedimentoPacienteResponseDTO save(ProcedimentoPacienteRequestDTO dto) {
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
                .observacoes(entity.getObservacoes())
                .paciente(paciente)
                .procedimento(procedimento)
                .build();

        var saved = repository.save(procedimentoPacienteToSave);
        return mapper.toResponse(saved);
    }

    public List<ProcedimentoPacienteResponseDTO> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public ProcedimentoPacienteResponseDTO findById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));
        return mapper.toResponse(entity);
    }

    public ProcedimentoPacienteResponseDTO findByPacienteId(UUID pacienteId) {
        var entity = repository.findByPacienteId(pacienteId);
        return mapper.toResponse(entity);
    }

    public ProcedimentoPacienteResponseDTO findByProcedimentoId(UUID procedimentoId) {
        var entity = repository.findByProcedimentoId(procedimentoId);
        return mapper.toResponse(entity);
    }

    public ProcedimentoPacienteResponseDTO update(UUID id, ProcedimentoPacienteRequestDTO dto) {
        var procedimentoPaciente = repository.findById(id)
                .orElseThrow(() -> new ProcedimentoNotFoundException(id));

        mapper.updateEntityFromDto(dto, procedimentoPaciente);

        if (dto.pacienteId() != null) {
            var paciente = pacienteRepository.findById(dto.pacienteId())
                    .orElseThrow(() -> new PacienteNotFoundException(dto.pacienteId()));
            procedimentoPaciente.setPaciente(paciente);
        }

        if (dto.procedimentoId() != null) {
            var procedimento = procedimentoRepository.findById(dto.procedimentoId())
                    .orElseThrow(() -> new ProcedimentoNotFoundException(dto.procedimentoId()));
            procedimentoPaciente.setProcedimento(procedimento);
        }

        var updated = repository.save(procedimentoPaciente);
        return mapper.toResponse(updated);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ProcedimentoNotFoundException(id);
        }
        repository.deleteById(id);
    }

}

