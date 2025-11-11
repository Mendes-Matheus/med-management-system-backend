package mendes.dev95.med_management_system_backend.domain.paciente.repository;

import io.lettuce.core.dynamic.annotation.Param;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByRg(String rg);
    Optional<Paciente> findByCns(String cns);
    Optional<Paciente> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
    boolean existsByCns(String cns);
    boolean existsByEmail(String email);
    Optional<Paciente> findByNome(String nome);


    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO(
            p.id,
            p.nome,
            p.dataNascimento,
            p.cpf,
            p.rg,
            p.cns,
            p.nomePai,
            p.nomeMae,
            p.telefone,
            p.telefoneSecundario,
            p.email,
            p.sexo,
            p.logradouro,
            p.numero,
            p.bairro,
            p.observacoes
        )
        FROM Paciente p
        ORDER BY p.nome
    """)
    Page<PacienteResponseDTO> findAllPacientes(Pageable pageable);

    @Query("""
        SELECT new mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO(
            p.id,
            p.nome,
            p.dataNascimento,
            p.cpf,
            p.rg,
            p.cns,
            p.nomePai,
            p.nomeMae,
            p.telefone,
            p.telefoneSecundario,
            p.email,
            p.sexo,
            p.logradouro,
            p.numero,
            p.bairro,
            p.observacoes
        )
        FROM Paciente p
        WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        ORDER BY p.nome ASC
    """)
    Page<PacienteResponseDTO> findByNomeContainingIgnoreCase(
            @Param("nome") String nome,
            Pageable pageable
    );



}