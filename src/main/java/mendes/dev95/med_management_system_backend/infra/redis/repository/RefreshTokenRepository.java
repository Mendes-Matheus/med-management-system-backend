package mendes.dev95.med_management_system_backend.infra.redis.repository;

import mendes.dev95.med_management_system_backend.infra.security.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}


