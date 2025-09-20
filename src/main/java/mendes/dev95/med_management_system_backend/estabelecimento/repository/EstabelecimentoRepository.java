package mendes.dev95.med_management_system_backend.estabelecimento.repository;

import mendes.dev95.med_management_system_backend.estabelecimento.entity.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, UUID> {
}