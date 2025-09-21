package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository;

import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcedimentoPacienteRepository extends JpaRepository<ProcedimentoPaciente, UUID> {

    ProcedimentoPaciente findByPacienteId(UUID pacienteId);

    ProcedimentoPaciente findByProcedimentoId(UUID procedimentoId);

    boolean existsByPacienteIdAndProcedimentoId(UUID pacienteId, UUID procedimentoId);

    boolean existsByPacienteIdAndProcedimentoIdAndStatusIn(
            UUID pacienteId,
            UUID procedimentoId,
            List<StatusProcedimento> status
    );
}
