package mendes.dev95.med_management_system_backend.domain.usuario.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioAuthResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.*;
import mendes.dev95.med_management_system_backend.domain.usuario.mapper.UsuarioMapper;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
import mendes.dev95.med_management_system_backend.infra.security.TokenService;
import mendes.dev95.med_management_system_backend.infra.security.service.RefreshTokenService;
import mendes.dev95.med_management_system_backend.infra.util.MaskUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MessageSource messageSource;
    private final UsuarioMapper mapper;
    private final RefreshTokenService refreshTokenService;

    public UsuarioAuthResponseDTO login(UsuarioLoginRequestDTO request) {
        String maskedEmail = MaskUtil.maskEmail(request.email());
        log.debug("Tentando autenticar usuário: {}", maskedEmail);

        var usuario = repository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Tentativa de login com e-mail inexistente: {}", maskedEmail);
                    return new InvalidCredentialsException(getMessage("usuario.invalidemailorpassword"));
                });

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            log.warn("Senha inválida para e-mail: {}", maskedEmail);
            throw new InvalidCredentialsException(getMessage("usuario.invalidemailorpassword"));
        }

        log.info("Usuário autenticado com sucesso: {}", maskedEmail);
        return createAuthResponse(usuario);
    }

    @Transactional
    public UsuarioAuthResponseDTO register(UsuarioRegisterRequestDTO request) {
        String maskedEmail = MaskUtil.maskEmail(request.email());
        log.debug("Tentando registrar usuário: {}", maskedEmail);

        var entity = mapper.toEntity(request);
        validateUser(entity);

        try {
            var usuario = mapper.toEntity(request);
            usuario.setPassword(passwordEncoder.encode(request.password()));

            var saved = repository.save(usuario);
            log.info("Usuário registrado com sucesso: {}", MaskUtil.maskEmail(saved.getEmail()));

            return createAuthResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            log.warn("Tentativa de registro com e-mail já existente: {}", maskedEmail);
            throw new UsuarioAlreadyExistsException(getMessage("usuario.email.alreadyexists"), ex);
        } catch (Exception ex) {
            log.error("Erro inesperado no registro de usuário: {}", maskedEmail, ex);
            throw new UsuarioRegistrationException(getMessage("usuario.registration.error"), ex);
        }
    }

    private void validateUser(Usuario usuario) {
        if (
            repository.existsByCpf(usuario.getCpf()) ||
            repository.existsByUsername(usuario.getUsername()) ||
            repository.existsById(usuario.getId()) ||
            repository.existsByEmail(usuario.getEmail())
        ) {
            throw new UsuarioAlreadyExistsException(getMessage("usuario.alreadyexists"));
        }
    }

    private UsuarioAuthResponseDTO createAuthResponse(Usuario usuario) {
        try {
            String accessToken = tokenService.generateAccessToken(usuario);
            String refreshToken = tokenService.generateRefreshToken(usuario);

            Instant refreshExp = tokenService.getExpiration(refreshToken).toInstant();

            refreshTokenService.create(usuario.getEmail(), refreshToken, refreshExp);

            return new UsuarioAuthResponseDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    accessToken,
                    refreshToken,
                    refreshExp
            );
        } catch (Exception ex) {
            log.error("Erro ao gerar token para: {}", MaskUtil.maskEmail(usuario.getEmail()), ex);
            throw new TokenGenerationException(getMessage("token.generation.error"), ex);
        }
    }

    public String extractJti(String token) {
        try {
            return tokenService.getJti(token);
        } catch (Exception ex) {
            log.warn("Falha ao extrair JTI do token");
            return null;
        }
    }

    public long remainingValidityMillis(String token) {
        try {
            Date exp = tokenService.getExpiration(token);
            return exp.getTime() - System.currentTimeMillis();
        } catch (Exception ex) {
            return 0;
        }
    }

    public String extractUsername(String token) {
        try {
            return tokenService.getUsername(token);
        } catch (Exception ex) {
            log.warn("Falha ao extrair username do token");
            return null;
        }
    }

    public UsuarioAuthResponseDTO generateTokens(String username) {
        var usuario = repository.findByEmail(username)
                .orElseThrow(() -> new InvalidCredentialsException(getMessage("usuario.invalidemailorpassword")));
        return createAuthResponse(usuario);
    }

    private String getMessage(@NonNull String code, @NonNull Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
