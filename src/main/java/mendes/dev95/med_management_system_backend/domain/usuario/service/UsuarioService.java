package mendes.dev95.med_management_system_backend.domain.usuario.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioUpdateRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioFetchException;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioNotFoundException;
import mendes.dev95.med_management_system_backend.domain.usuario.exception.UsuarioUpdateException;
import mendes.dev95.med_management_system_backend.domain.usuario.mapper.UsuarioMapper;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper mapper;
    private final MessageSource messageSource;

    public UsuarioResponseDTO findUsuarioByEmail(String email) {
        log.debug("Searching for user with email: {}", email);

        var usuario = repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsuarioNotFoundException(getMessage("usuario.notfound"));
                });

        log.debug("User found: {}", email);
        return mapper.toResponse(usuario);
    }

    public UsuarioResponseDTO findUsuarioById(UUID id) {
        log.debug("Searching for user with id: {}", id);

        var usuario = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new UsuarioNotFoundException(getMessage("usuario.notfound"));
                });

        log.debug("User found: {}", id);
        return mapper.toResponse(usuario);
    }


    public List<UsuarioResponseDTO> findAllUsuarios() {
        log.debug("Fetching all users");

        try {
            List<Usuario> usuarios = repository.findAll();
            List<UsuarioResponseDTO> response = mapper.toResponseList(usuarios);
            log.debug("Found {} users", response.size());
            return response;
        } catch (Exception ex) {
            log.error("Error fetching all users", ex);
            throw new UsuarioFetchException(getMessage("usuario.fetch.error"), ex);
        }
    }

    @Transactional
    public UsuarioResponseDTO update(UsuarioUpdateRequestDTO request) {
        log.debug("Attempting to update user with email: {}", request.email());

        try {
            Usuario usuario = repository.findByEmail(request.email())
                    .orElseThrow(() -> {
                        log.warn("Attempted to update non-existent user: {}", request.email());
                        return new UsuarioNotFoundException(getMessage("usuario.notfound"));
                    });

            updateExistingUser(usuario, request);
            Usuario savedUsuario = repository.save(usuario);
            log.info("User successfully updated: {}", savedUsuario.getEmail());
            return mapper.toResponse(savedUsuario);
        } catch (UsuarioNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating user with email: {}", request.email(), ex);
            throw new UsuarioUpdateException(getMessage("usuario.update.error"), ex);
        }
    }

    private void updateExistingUser(Usuario existingUser, UsuarioUpdateRequestDTO request) {
        log.debug("Updating existing user: {}", request.email());
        mapper.updateEntityFromDto(request, existingUser);

        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }
    }

    private String getMessage(@NonNull String code, @NonNull Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
