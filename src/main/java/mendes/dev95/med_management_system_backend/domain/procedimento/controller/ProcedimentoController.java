package mendes.dev95.med_management_system_backend.domain.procedimento.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.mapper.ProcedimentoMapper;
import mendes.dev95.med_management_system_backend.domain.procedimento.service.ProcedimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoService service;
    private final ProcedimentoMapper mapper;

    @SuppressWarnings("DuplicatedCode")
    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> save(@RequestBody @Valid ProcedimentoRequestDTO requestDTO) {
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
    public ResponseEntity<List<ProcedimentoResponseDTO>> findAll() {
        var entities = service.findAll();
        var responses = mapper.toResponseList(entities);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> findById(@PathVariable UUID id) {
        var entity = service.findById(id);
        return ResponseEntity.ok(mapper.toResponse(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid ProcedimentoRequestDTO request) {
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
