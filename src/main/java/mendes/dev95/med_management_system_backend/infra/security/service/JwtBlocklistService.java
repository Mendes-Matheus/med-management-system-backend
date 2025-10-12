package mendes.dev95.med_management_system_backend.infra.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Serviço responsável por armazenar e consultar tokens revogados (JWTs) no Redis.
 * Utilizado no fluxo de logout e validação de tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtBlocklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "jwt:block:";

    public void blockJti(String jti, Duration ttl) {
        String key = PREFIX + jti;
        redisTemplate.opsForValue().set(key, "blocked", ttl);
    }

    public boolean isBlocked(String jti) {
        String key = PREFIX + jti;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

