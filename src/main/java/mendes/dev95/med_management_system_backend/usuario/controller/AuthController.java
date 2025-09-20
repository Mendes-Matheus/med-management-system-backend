package mendes.dev95.med_management_system_backend.usuario.controller;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.usuario.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@RequestBody UsuarioLoginRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody UsuarioRegisterRequestDTO request){
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/user")
    public ResponseEntity<UsuarioResponseDTO> findUsuario(@RequestParam String email){
        return ResponseEntity.ok(authService.findUsuarioByEmail(email));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> findAllUsuarios(){
        return ResponseEntity.ok(authService.findAllUsuarios());
    }

    @PostMapping("/update-user")
    public ResponseEntity<UsuarioResponseDTO> update(@RequestBody UsuarioRegisterRequestDTO request){
        return ResponseEntity.ok(authService.update(request));
    }
}
