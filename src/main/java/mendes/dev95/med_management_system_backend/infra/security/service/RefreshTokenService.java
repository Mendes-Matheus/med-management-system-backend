package mendes.dev95.med_management_system_backend.infra.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar Refresh Tokens usando Redis.
 * Substitui o repositório JPA por cache distribuído de alta performance.
 * Cada token é armazenado com chave "refresh:{username}" e expira automaticamente.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "jwt:refresh:";

    /**
     * Cria e armazena um novo refresh token no Redis.
     * @param username email/username do usuário
     * @param token token gerado
     * @param expiresAt instante de expiração
     */
    public void create(String username, String token, Instant expiresAt) {
        String key = PREFIX + username;
        long ttlSeconds = Duration.between(Instant.now(), expiresAt).toSeconds();

        // Armazena o token com tempo de expiração
        redisTemplate.opsForValue().set(key, token, Duration.ofSeconds(ttlSeconds));
    }

    /**
     * Valida um refresh token (verifica se existe e se corresponde ao usuário).
     * @param username e-mail do usuário
     * @param token token recebido do cliente
     * @return true se for válido, false caso contrário
     */
    public boolean validate(String username, String token) {
        String key = PREFIX + username;
        String stored = redisTemplate.opsForValue().get(key);
        return stored != null && stored.equals(token);
    }

    /**
     * Revoga o refresh token atual de um usuário (ex: logout).
     * @param username e-mail do usuário
     */
    public void revoke(String username) {
        redisTemplate.delete(PREFIX + username);
    }

    /**
     * Obtém o refresh token atual de um usuário (se existir).
     */
    public Optional<String> find(String username) {
        String token = redisTemplate.opsForValue().get(PREFIX + username);
        return Optional.ofNullable(token);
    }
}
