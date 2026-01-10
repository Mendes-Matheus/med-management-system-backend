package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.TipoProcedimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProcedimentoPacienteRepositoryCustomImpl implements ProcedimentoPacienteRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByFiltro(
            String cpf,
            LocalDate dataInicio,
            LocalDate dataFim,
            String status,
            UUID procedimentoId,
            Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProcedimentoPaciente> query = cb.createQuery(ProcedimentoPaciente.class);
        Root<ProcedimentoPaciente> root = query.from(ProcedimentoPaciente.class);

        // Join com as entidades relacionadas
        Join<ProcedimentoPaciente, Procedimento> procedimentoJoin = root.join("procedimento");
        Join<ProcedimentoPaciente, Paciente> pacienteJoin = root.join("paciente");

        List<Predicate> predicates = new ArrayList<>();

        // Filtro por tipo CONSULTA
        predicates.add(cb.equal(
                procedimentoJoin.get("tipoProcedimento"),
                TipoProcedimento.CONSULTA
        ));

        // Filtro por CPF
        if (cpf != null && !cpf.trim().isEmpty()) {
            predicates.add(cb.like(
                    pacienteJoin.get("cpf"),
                    "%" + cpf + "%"
            ));
        }

        // Filtro por data início
        if (dataInicio != null) {
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get("dataSolicitacao"),
                    dataInicio
            ));
        }

        // Filtro por data fim
        if (dataFim != null) {
            predicates.add(cb.lessThanOrEqualTo(
                    root.get("dataSolicitacao"),
                    dataFim
            ));
        }

        // Filtro por status
        if (status != null && !status.trim().isEmpty()) {
            try {
                StatusProcedimento statusEnum = StatusProcedimento.valueOf(status.toUpperCase());
                predicates.add(cb.equal(root.get("status"), statusEnum));
            } catch (IllegalArgumentException e) {
                // Ignora status inválido
            }
        }

        // Filtro por procedimento
        if (procedimentoId != null) {
            predicates.add(cb.equal(
                    procedimentoJoin.get("id"),
                    procedimentoId
            ));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("dataSolicitacao")));

        // Query para os dados
        TypedQuery<ProcedimentoPaciente> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ProcedimentoPaciente> resultList = typedQuery.getResultList();

        // Converte para DTO
        List<ProcedimentoPacienteSimpleResponseDTO> content = resultList.stream()
                .map(this::toSimpleResponseDTO)
                .toList();

        // Query para contar total
        Long total = getTotalCount(predicates);

        return new PageImpl<>(content, pageable, total);
    }

    private Long getTotalCount(List<Predicate> predicates) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProcedimentoPaciente> root = countQuery.from(ProcedimentoPaciente.class);

        Join<ProcedimentoPaciente, Procedimento> procedimentoJoin = root.join("procedimento");
        Join<ProcedimentoPaciente, Paciente> pacienteJoin = root.join("paciente");

        // Adiciona o filtro de CONSULTA
        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(
                procedimentoJoin.get("tipoProcedimento"),
                TipoProcedimento.CONSULTA
        ));

        // Adiciona os outros predicados (exceto o de CONSULTA que já foi adicionado)
        for (Predicate predicate : predicates) {
            if (!predicate.toString().contains("tipoProcedimento")) {
                countPredicates.add(predicate);
            }
        }

        countQuery.select(cb.count(root));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private ProcedimentoPacienteSimpleResponseDTO toSimpleResponseDTO(ProcedimentoPaciente pp) {
        return new ProcedimentoPacienteSimpleResponseDTO(
                pp.getId(),
                pp.getStatus(),
                pp.getRetorno(),
                pp.getDataSolicitacao(),
                pp.getDataAgendamento(),
                new PacienteSimpleResponseDTO(
                        pp.getPaciente().getNome(),
                        pp.getPaciente().getCpf(),
                        pp.getPaciente().getRg(),
                        pp.getPaciente().getCns(),
                        pp.getPaciente().getTelefone()
                ),
                new ProcedimentoSimpleResponseDTO(
                        pp.getProcedimento().getId(),
                        pp.getProcedimento().getNomeProcedimento(),
                        pp.getProcedimento().getTipoProcedimento(),
                        pp.getProcedimento().getOrientacoes(),
                        pp.getProcedimento().getObservacoes()
                )
        );
    }
}