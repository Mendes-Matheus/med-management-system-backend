package mendes.dev95.med_management_system_backend.domain.usuario.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioAuthResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioAuthResponseDTO> login(@Valid @RequestBody UsuarioLoginRequestDTO request) {
        UsuarioAuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioAuthResponseDTO> register(@Valid @RequestBody UsuarioRegisterRequestDTO request) {
        UsuarioAuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
