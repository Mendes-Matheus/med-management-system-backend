package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteFullResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteUpdateDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProcedimentoPacienteMapper {

    // ==========================================
    // ============== ENTRADA ===================
    // ==========================================

    default ProcedimentoPaciente toEntity(ProcedimentoPacienteRequestDTO dto) {
        if (dto == null) return null;

        var paciente = new Paciente();
        paciente.setId(dto.pacienteId());

        var procedimento = new Procedimento();
        procedimento.setId(dto.procedimentoId());

        return ProcedimentoPaciente.builder()
                .dataSolicitacao(dto.dataSolicitacao())
                .observacoes(dto.observacoes())
                .paciente(paciente)
                .procedimento(procedimento)
                .build();
    }

    // ==========================================
    // ============== SAÍDAS ====================
    // ==========================================

    /**
     * Mapeia entidade -> DTO resumido (listagens, tabelas, etc.)
     */
    default ProcedimentoPacienteSimpleResponseDTO toSimpleResponse(ProcedimentoPaciente entity) {
        if (entity == null) return null;

        var paciente = entity.getPaciente();
        var pacienteDTO = new PacienteSimpleResponseDTO(
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getRg(),
                paciente.getCns(),
                paciente.getTelefone()
        );

        var procedimentoDTO = toProcedimentoSimpleResponse(entity.getProcedimento());

        return new ProcedimentoPacienteSimpleResponseDTO(
                entity.getId(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getDataSolicitacao(),
                entity.getDataAgendamento(),
                pacienteDTO,
                procedimentoDTO
        );
    }

    /**
     * Mapeia entidade -> DTO detalhado (findById, detalhes completos)
     */
    default ProcedimentoPacienteResponseDTO toFullResponse(ProcedimentoPaciente entity) {
        if (entity == null) return null;

        var pacienteDTO = toPacienteResponse(entity.getPaciente());
        var procedimentoDTO = toProcedimentoFullResponse(entity.getProcedimento());

        return new ProcedimentoPacienteResponseDTO(
                entity.getId(),
                entity.getStatus() != null ? entity.getStatus().name() : null,
                entity.getDataSolicitacao(),
                entity.getDataAgendamento(),
                entity.getObservacoes(),
                pacienteDTO,
                procedimentoDTO
        );
    }

    // ==========================================
    // ============== SUPORTES ==================
    // ==========================================

    private PacienteFullResponseDTO toPacienteResponse(Paciente paciente) {
        if (paciente == null) return null;

        return new PacienteFullResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getDataNascimento(),
                paciente.getCpf(),
                paciente.getRg(),
                paciente.getCns(),
                paciente.getNomePai(),
                paciente.getNomeMae(),
                paciente.getTelefone(),
                paciente.getTelefoneSecundario(),
                paciente.getEmail(),
                paciente.getSexo(),
                paciente.getLogradouro(),
                paciente.getNumero(),
                paciente.getBairro(),
                paciente.getObservacoes()
        );
    }

    private ProcedimentoSimpleResponseDTO toProcedimentoSimpleResponse(Procedimento procedimento) {
        if (procedimento == null) return null;

        var estabelecimentos = procedimento.getEstabelecimentos() == null
                ? Collections.<EstabelecimentoSimpleResponseDTO>emptyList()
                : procedimento.getEstabelecimentos().stream()
                .map(est -> new EstabelecimentoSimpleResponseDTO(est.getNomeEstabelecimento()))
                .toList();

        return new ProcedimentoSimpleResponseDTO(
                procedimento.getId(),
                procedimento.getNomeProcedimento(),
                procedimento.getTipoProcedimento(),
                procedimento.getObservacoes(),
                procedimento.getOrientacoes(),
                estabelecimentos
        );
    }

    private ProcedimentoResponseDTO toProcedimentoFullResponse(Procedimento procedimento) {
        if (procedimento == null) return null;

        var estabelecimentos = procedimento.getEstabelecimentos() == null
                ? Collections.<EstabelecimentoSimpleResponseDTO>emptyList()
                : procedimento.getEstabelecimentos().stream()
                .map(est -> new EstabelecimentoSimpleResponseDTO(est.getNomeEstabelecimento()))
                .toList();

        return new ProcedimentoResponseDTO(
                procedimento.getId(),
                procedimento.getNomeProcedimento(),
                procedimento.getTipoProcedimento(),
                procedimento.getOrientacoes(),
                procedimento.getObservacoes(),
                estabelecimentos
        );
    }

    // ==========================================
    // ============ LISTA =======================
    // ==========================================

    default List<ProcedimentoPacienteSimpleResponseDTO> toSimpleResponseList(List<ProcedimentoPaciente> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toSimpleResponse)
                .toList();
    }

    // ==========================================
    // ============= UPDATE =====================
    // ==========================================

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "procedimento", ignore = true)
    void updateEntityFromDto(ProcedimentoPacienteUpdateDTO dto, @MappingTarget ProcedimentoPaciente entity);
}
