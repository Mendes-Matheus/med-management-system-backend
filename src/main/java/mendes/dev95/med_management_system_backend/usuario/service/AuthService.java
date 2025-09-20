package mendes.dev95.med_management_system_backend.usuario.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.infra.security.TokenService;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.usuario.entity.Usuario;
import mendes.dev95.med_management_system_backend.usuario.mapper.UsuarioMapper;
import mendes.dev95.med_management_system_backend.usuario.repository.UsuarioRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper userMapper;
    private final MessageSource messageSource;

    public UsuarioResponseDTO login(UsuarioLoginRequestDTO request) {
        Usuario usuario = repository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        getMessage("user.invalidemailorpassword"))
                );

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    getMessage("user.invalidemailorpassword")
            );
        }

        String token = tokenService.generateToken(usuario);
        UsuarioResponseDTO response = userMapper.toResponse(usuario);
        return new UsuarioResponseDTO(response.id(), response.username(), response.email(), token);
    }

    public UsuarioResponseDTO register(UsuarioRegisterRequestDTO request) {
        Optional<Usuario> user = repository.findByEmail(request.email());

        if (user.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    getMessage("user.email.alreadyexists")
            );
        }

        Usuario newUsuario = userMapper.toEntity(request);
        newUsuario.setPassword(passwordEncoder.encode(request.password()));

        repository.save(newUsuario);

        String token = tokenService.generateToken(newUsuario);
        UsuarioResponseDTO response = userMapper.toResponse(newUsuario);
        return new UsuarioResponseDTO(response.id(), response.username(), response.email(), token);
    }

    public UsuarioResponseDTO findUsuarioByEmail(String email) {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        getMessage("user.notfound"))
                );

        String token = tokenService.generateToken(usuario);
        UsuarioResponseDTO response = userMapper.toResponse(usuario);
        return new UsuarioResponseDTO(response.id(), response.username(), response.email(), token);
    }

    public List<UsuarioResponseDTO> findAllUsuarios() {
        List<Usuario> usuarios = repository.findAll();
        List<UsuarioResponseDTO> response = userMapper.toResponseList(usuarios);
        return response;
    }

    public UsuarioResponseDTO update(UsuarioRegisterRequestDTO request) {
        Optional<Usuario> existingUsuario = repository.findByEmail(request.email());

        Usuario usuario;
        if (existingUsuario.isPresent()) {
            usuario = existingUsuario.get();
            userMapper.updateEntityFromDto(request, usuario);
            usuario.setPassword(passwordEncoder.encode(request.password()));
        } else {
            usuario = userMapper.toEntity(request);
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        repository.save(usuario);

        String token = tokenService.generateToken(usuario);
        UsuarioResponseDTO response = userMapper.toResponse(usuario);
        return new UsuarioResponseDTO(response.id(), response.username(), response.email(), token);
    }

    private String getMessage(@NonNull String code, @NonNull Object... args) {
        Locale locale = LocaleContextHolder.getLocale(); // detecta locale do request
        return messageSource.getMessage(code, args, locale);
    }
}
