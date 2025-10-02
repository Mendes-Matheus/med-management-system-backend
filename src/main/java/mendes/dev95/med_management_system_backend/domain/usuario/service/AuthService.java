package mendes.dev95.med_management_system_backend.domain.usuario.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioAuthResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.InvalidCredentialsException;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.TokenGenerationException;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioAlreadyExistsException;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioRegistrationException;
import mendes.dev95.med_management_system_backend.domain.usuario.mapper.UsuarioMapper;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
import mendes.dev95.med_management_system_backend.infra.security.TokenService;
import mendes.dev95.med_management_system_backend.infra.util.MaskUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MessageSource messageSource;
    private final UsuarioMapper mapper;

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

    private UsuarioAuthResponseDTO createAuthResponse(Usuario usuario) {
        try {
            String token = tokenService.generateToken(usuario);
            return new UsuarioAuthResponseDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail(),
                    token
            );
        } catch (Exception ex) {
            log.error("Erro ao gerar token para: {}", MaskUtil.maskEmail(usuario.getEmail()), ex);
            throw new TokenGenerationException(getMessage("token.generation.error"), ex);
        }
    }

    private String getMessage(@NonNull String code, @NonNull Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}

