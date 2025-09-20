package mendes.dev95.med_management_system_backend.domain.usuario.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.domain.usuario.repository.UsuarioRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository userRepository;
    private final MessageSource messageSource;

    public Optional<Usuario> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void verifyIfUsuarioExists(Usuario usuario) {
        if(userRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ja tem um usuario cadastrado com esse email");
        }
        if (userRepository.findById(usuario.getId()).isPresent()) {
            throw new IllegalArgumentException("Ja tem um usuario cadastrado com esse id");
        }
    }

    public Usuario save(Usuario usuario) {
        verifyIfUsuarioExists(usuario);
        return userRepository.save(usuario);
    }

    public Optional<Usuario> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<Usuario> findAll() {
        return userRepository.findAll();
    }

    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
