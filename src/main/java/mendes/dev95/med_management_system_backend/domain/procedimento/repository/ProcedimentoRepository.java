package mendes.dev95.med_management_system_backend.domain.procedimento.repository;

import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {
}
