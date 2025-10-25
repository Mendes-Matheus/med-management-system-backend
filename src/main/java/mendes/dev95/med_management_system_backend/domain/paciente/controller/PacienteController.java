package mendes.dev95.med_management_system_backend.domain.paciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.*;
import mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @PostMapping
    public ResponseEntity<PacienteFullResponseDTO> save(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        var response = service.save(requestDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("procedimentos/{id}")
    public ResponseEntity<PacienteResponseWithProcedimentosDTO> findProcedimentosPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findProcedimentosPaciente(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PacienteResponseDTO> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PacienteResponseDTO> findByNome(@PathVariable String nome) {
        return ResponseEntity.ok(service.findByNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid PacienteUpdateRequestDTO request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
