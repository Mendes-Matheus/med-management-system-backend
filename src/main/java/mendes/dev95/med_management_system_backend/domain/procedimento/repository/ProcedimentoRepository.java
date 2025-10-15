package mendes.dev95.med_management_system_backend.domain.procedimento.repository;

import mendes.dev95.med_management_system_backend.domain.estabelecimento.entity.Estabelecimento;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {

    List<Procedimento> findByEstabelecimentosContaining(Estabelecimento estabelecimento);
}
