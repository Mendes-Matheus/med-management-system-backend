package mendes.dev95.med_management_system_backend.usuario.controller;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.usuario.dto.UsuarioResponseDTO;
import mendes.dev95.med_management_system_backend.usuario.mapper.UsuarioMapper;
import mendes.dev95.med_management_system_backend.usuario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }
}
