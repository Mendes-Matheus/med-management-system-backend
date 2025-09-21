package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.mapper.ProcedimentoPacienteMapper;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.service.ProcedimentoPacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedimentos-paciente")
@RequiredArgsConstructor
public class ProcedimentoPacienteController {

    private final ProcedimentoPacienteService service;
    private final ProcedimentoPacienteMapper mapper;

    @PostMapping
    public ResponseEntity<ProcedimentoPacienteResponseDTO> save(@RequestBody @Valid ProcedimentoPacienteRequestDTO request) {
        var entity = mapper.toEntity(request);
        var created = service.save(entity);
        var response = mapper.toResponse(created);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProcedimentoPacienteResponseDTO>> findAll() {
        var find = service.findAll();
        var response = mapper.toResponseList(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoPacienteResponseDTO> findById(@PathVariable UUID id) {
        var find = service.findById(id);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<ProcedimentoPacienteResponseDTO> findByPacienteId(@PathVariable UUID id) {
        var find = service.findByPacienteId(id);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/procedimento/{id}")
    public ResponseEntity<ProcedimentoPacienteResponseDTO> findByProcedimentoId(@PathVariable UUID id) {
        var find = service.findByProcedimentoId(id);
        var response = mapper.toResponse(find);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoPacienteResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid ProcedimentoPacienteRequestDTO request) {
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