package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.service;

import lombok.RequiredArgsConstructor;
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

    public List<ProcedimentoPacienteSimpleResponseDTO> findAll() {
        return mapper.toSimpleResponseList(repository.findAll());
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

    public ProcedimentoPacienteSimpleResponseDTO findByProcedimentoId(UUID procedimentoId) {
        var entity = repository.findByProcedimentoId(procedimentoId);
        return mapper.toSimpleResponse(entity);
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

}

