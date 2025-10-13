package mendes.dev95.med_management_system_backend.domain.usuario.repository;

import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByUsername(String username);
}
