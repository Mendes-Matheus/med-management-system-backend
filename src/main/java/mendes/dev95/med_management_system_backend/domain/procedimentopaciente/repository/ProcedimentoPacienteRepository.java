package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.Pattern;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProcedimentoPacienteRepository extends JpaRepository<ProcedimentoPaciente, UUID> {

    ProcedimentoPaciente findByPacienteId(UUID pacienteId);


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pp.status = :status
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findByProcedimentoStatus(
            @Param("status") StatusProcedimento status,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pp.status = :status
            AND p.tipoProcedimento = 'CONSULTA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByProcedimentoStatus(
            @Param("status") StatusProcedimento status,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pp.status = :status
            AND p.tipoProcedimento = 'EXAME'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByProcedimentoStatus(
            @Param("status") StatusProcedimento status,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pp.status = :status
            AND p.tipoProcedimento = 'CIRURGIA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByProcedimentoStatus(
            @Param("status") StatusProcedimento status,
            Pageable pageable
    );


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.id = :procedimentoId
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findByProcedimentoId(
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.id = :procedimentoId
            AND p.tipoProcedimento = 'CONSULTA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByProcedimentoId(
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.id = :procedimentoId
            AND p.tipoProcedimento = 'EXAME'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByProcedimentoId(
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.id = :procedimentoId
            AND p.tipoProcedimento = 'CIRURGIA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByProcedimentoId(
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pa.cpf = :cpf
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findByPacienteCpf(@Param("cpf") String cpf, Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pa.cpf = :cpf
                AND p.tipoProcedimento = 'EXAME'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByPacienteCpf(@Param("cpf") String cpf, Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pa.cpf = :cpf
                AND p.tipoProcedimento = 'CIRURGIA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByPacienteCpf(@Param("cpf") String cpf, Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE pa.cpf = :cpf
                AND p.tipoProcedimento = 'CONSULTA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByPacienteCpf(@Param("cpf") String cpf, Pageable pageable);

    boolean existsByPacienteIdAndProcedimentoId(UUID pacienteId, UUID procedimentoId);

    boolean existsByPacienteIdAndProcedimentoIdAndStatusIn(
            UUID pacienteId,
            UUID procedimentoId,
            List<StatusProcedimento> status
    );


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.observacoes,
                p.orientacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.paciente pa
        JOIN pp.procedimento p
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findAllProcedimentos(Pageable pageable);


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CONSULTA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findAllConsultas(Pageable pageable);


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'EXAME'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findAllExames(Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CIRURGIA'
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findAllCirurgias(Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id, pp.status, pp.retorno, pp.dataSolicitacao, pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome, pa.cpf, pa.rg, pa.cns, pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id, p.nomeProcedimento, p.tipoProcedimento, p.orientacoes, p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CONSULTA'
            AND (:cpf IS NULL OR pa.cpf LIKE CONCAT('%', CAST(:cpf AS string), '%'))
            AND (:nome IS NULL OR pa.nome LIKE CONCAT('%', CAST(:nome AS string), '%'))
            AND (:status IS NULL OR pp.status = :status)
            AND (:procedimentoId IS NULL OR p.id = :procedimentoId)
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByFiltro(
            @Param("cpf") String cpf,
            @Param("nome") String nome,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("status") StatusProcedimento status,
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id, pp.status, pp.retorno, pp.dataSolicitacao, pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome, pa.cpf, pa.rg, pa.cns, pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id, p.nomeProcedimento, p.tipoProcedimento, p.orientacoes, p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'EXAME'
            AND (:cpf IS NULL OR pa.cpf LIKE CONCAT('%', CAST(:cpf AS string), '%'))
            AND (:nome IS NULL OR pa.nome LIKE CONCAT('%', CAST(:nome AS string), '%'))
            AND (:status IS NULL OR pp.status = :status)
            AND (:procedimentoId IS NULL OR p.id = :procedimentoId)
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findExamesByFiltro(
            @Param("cpf") String cpf,
            @Param("nome") String nome,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("status") StatusProcedimento status,
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id, pp.status, pp.retorno, pp.dataSolicitacao, pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome, pa.cpf, pa.rg, pa.cns, pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id, p.nomeProcedimento, p.tipoProcedimento, p.orientacoes, p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CIRURGIA'
            AND (:cpf IS NULL OR pa.cpf LIKE CONCAT('%', CAST(:cpf AS string), '%'))
            AND (:nome IS NULL OR pa.nome LIKE CONCAT('%', CAST(:nome AS string), '%'))
            AND (:status IS NULL OR pp.status = :status)
            AND (:procedimentoId IS NULL OR p.id = :procedimentoId)
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasByFiltro(
            @Param("cpf") String cpf,
            @Param("nome") String nome,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("status") StatusProcedimento status,
            @Param("procedimentoId") UUID procedimentoId,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CONSULTA'
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasBetweenDates(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'EXAME'
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findExamesBetweenDates(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO(
            pp.id,
            pp.status,
            pp.retorno,
            pp.dataSolicitacao,
            pp.dataAgendamento,
            new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO(
                pa.nome,
                pa.cpf,
                pa.rg,
                pa.cns,
                pa.telefone
            ),
            new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
                p.id,
                p.nomeProcedimento,
                p.tipoProcedimento,
                p.orientacoes,
                p.observacoes
            )
        )
        FROM ProcedimentoPaciente pp
        JOIN pp.procedimento p
        JOIN pp.paciente pa
        WHERE p.tipoProcedimento = 'CIRURGIA'
            AND pp.dataSolicitacao BETWEEN :dataInicio AND :dataFim
        ORDER BY pp.dataSolicitacao DESC
    """)
    Page<ProcedimentoPacienteSimpleResponseDTO> findCirurgiasBetweenDates(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

}
