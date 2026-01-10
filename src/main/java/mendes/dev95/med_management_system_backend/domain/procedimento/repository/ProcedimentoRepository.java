package mendes.dev95.med_management_system_backend.domain.procedimento.repository;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.entity.Estabelecimento;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {

    List<Procedimento> findByEstabelecimentosContaining(Estabelecimento estabelecimento);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoSimpleResponseDTO(
            p.id,
            p.nomeProcedimento,
            p.tipoProcedimento,
            p.orientacoes,
            p.observacoes
        )
        FROM Procedimento p
        ORDER BY p.nomeProcedimento ASC
    """)
    List<ProcedimentoSimpleResponseDTO> findAllProcedimentos();

}
