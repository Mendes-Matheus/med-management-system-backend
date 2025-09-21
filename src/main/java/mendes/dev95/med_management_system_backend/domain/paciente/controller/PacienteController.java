package mendes.dev95.med_management_system_backend.domain.paciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.PacienteResponseWithProcedimentosDTO;
import mendes.dev95.med_management_system_backend.domain.paciente.mapper.PacienteMapper;
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
    private final PacienteMapper mapper;

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> save(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        var entity = mapper.toEntity(requestDTO);
        var created = service.save(entity);
        var response = mapper.toResponse(created);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> findAll() {
        var find = service.findAll();
        var response = mapper.toResponseList(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> findById(@PathVariable UUID id) {
        var find = service.findById(id);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("procedimentos/{id}")
    public ResponseEntity<PacienteResponseWithProcedimentosDTO> findProcedimentosPaciente(@PathVariable UUID id) {
        var find = service.findById(id);
        var response = mapper.toDetailResponse(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PacienteResponseDTO> findByCpf(@PathVariable String cpf) {
        var find = service.findByCpf(cpf);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PacienteResponseDTO> findByNome(@PathVariable String nome) {
        var find = service.findByNome(nome);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid PacienteRequestDTO request) {
        var updated = service.update(id, request);
        var response = mapper.toResponse(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}