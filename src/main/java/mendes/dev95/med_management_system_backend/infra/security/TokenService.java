package mendes.dev95.med_management_system_backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Serviço responsável por gerar e validar tokens JWT (Access e Refresh).
 */
@Service
@RequiredArgsConstructor
public class TokenService {

//    @Autowired
//    private JwtBlocklistService jwtBlocklistService;
//
//    private  JWTVerifier jwtVerifier;

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    /** Tempo padrão de expiração do Access Token (15 minutos) */
    @Value("${api.security.token.access-expiration-seconds:900}")
    private Long accessExpirationSeconds;

    /** Tempo padrão de expiração do Refresh Token (7 dias) */
    @Value("${api.security.token.refresh-expiration-seconds:604800}")
    private Long refreshExpirationSeconds;

    /**
     * Gera um Access Token com curta duração.
     */
    public String generateAccessToken(Usuario usuario) {
        return generateToken(usuario, accessExpirationSeconds, "access");
    }

    /**
     * Gera um Refresh Token com longa duração.
     */
    public String generateRefreshToken(Usuario usuario) {
        return generateToken(usuario, refreshExpirationSeconds, "refresh");
    }

    /**
     * Metodo interno para gerar token generico (Access ou Refresh).
     */
    private String generateToken(Usuario usuario, Long expirationSeconds, String type) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            List<String> authorities = usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            Instant now = Instant.now();
            Instant expiresAt = now.plusSeconds(expirationSeconds);

            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId().toString())
                    .withClaim("email", usuario.getEmail())
                    .withClaim("authorities", authorities)
                    .withClaim("type", type)
                    .withJWTId(UUID.randomUUID().toString()) // gera o JTI
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(expiresAt))
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "token.errorwhilecreatingtoken", ex);
        }
    }

    /**
     * Valida e retorna o JWT decodificado (ou null se inválido).
     */
    public DecodedJWT validateAndGetDecoded(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    /**
     * Extrai o e-mail (subject) do token.
     */
    public String getUsername(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Extrai a data de expiração do token.
     */
    public Date getExpiration(String token) {
        try {
            return JWT.decode(token).getExpiresAt();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Extrai o JTI (JWT ID) usado na revogação.
     */
    public String getJti(String token) {
        try {
            return JWT.decode(token).getId();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Metodo legado para compatibilidade com código antigo (gera Access Token padrão).
     */
    public String generateToken(Usuario usuario) {
        return generateAccessToken(usuario);
    }
}
