package mendes.dev95.med_management_system_backend.domain.paciente.repository;

import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByRg(String rg);
    Optional<Paciente> findByCns(String cns);
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
    boolean existsByCns(String cns);
    Optional<Paciente> findByNome(String nome);
}