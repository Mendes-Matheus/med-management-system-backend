package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteSimpleResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.ProcedimentoPacienteUpdateDTO;
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

    @PostMapping
    public ResponseEntity<ProcedimentoPacienteSimpleResponseDTO> save(@RequestBody @Valid ProcedimentoPacienteRequestDTO request) {
        var response = service.save(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProcedimentoPacienteSimpleResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoPacienteResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<ProcedimentoPacienteSimpleResponseDTO> findByPacienteId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findByPacienteId(id));
    }

    @GetMapping("/procedimento/{id}")
    public ResponseEntity<ProcedimentoPacienteSimpleResponseDTO> findByProcedimentoId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findByProcedimentoId(id));
    }

    @GetMapping("/paciente/cpf/{cpf}")
    public ResponseEntity<List<ProcedimentoPacienteSimpleResponseDTO>> findByPacienteCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByPacienteCpf(cpf));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoPacienteSimpleResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProcedimentoPacienteUpdateDTO request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
