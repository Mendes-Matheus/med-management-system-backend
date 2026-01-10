package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.repository;

import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface ProcedimentoPacienteRepositoryCustom {
    Page<ProcedimentoPacienteSimpleResponseDTO> findConsultasByFiltro(
            String cpf,
            LocalDate dataInicio,
            LocalDate dataFim,
            String status,
            UUID procedimentoId,
            Pageable pageable
    );
}