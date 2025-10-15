package mendes.dev95.med_management_system_backend.domain.procedimento.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoResponseDTO;
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

    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> save(@RequestBody @Valid ProcedimentoRequestDTO requestDTO) {
        var response = service.save(requestDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProcedimentoResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProcedimentoRequestDTO request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/estabelecimentos/{estabelecimentoId}")
    public ResponseEntity<ProcedimentoResponseDTO> associarEstabelecimento(
            @PathVariable UUID id,
            @PathVariable UUID estabelecimentoId
    ) {
        return ResponseEntity.ok(service.associarEstabelecimento(id, estabelecimentoId));
    }

    @DeleteMapping("/{id}/estabelecimentos/{estabelecimentoId}")
    public ResponseEntity<ProcedimentoResponseDTO> removerEstabelecimento(
            @PathVariable UUID id,
            @PathVariable UUID estabelecimentoId
    ) {
        return ResponseEntity.ok(service.removerEstabelecimento(id, estabelecimentoId));
    }

    @GetMapping("/estabelecimento/{estabelecimentoId}")
    public ResponseEntity<List<ProcedimentoResponseDTO>> findByEstabelecimento(
            @PathVariable UUID estabelecimentoId
    ) {
        return ResponseEntity.ok(service.findByEstabelecimentoId(estabelecimentoId));
    }
}

