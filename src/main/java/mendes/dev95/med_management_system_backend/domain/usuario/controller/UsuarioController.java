package mendes.dev95.med_management_system_backend.domain.usuario.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioUpdateRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        List<UsuarioResponseDTO> usuarios = usuarioService.findAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> findByEmail(@PathVariable String email) {
        UsuarioResponseDTO usuario = usuarioService.findUsuarioByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable UUID id) {
        UsuarioResponseDTO usuario = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> update(@Valid @RequestBody UsuarioRegisterRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
