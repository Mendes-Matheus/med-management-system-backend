package mendes.dev95.med_management_system_backend.domain.estabelecimento.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.service.EstabelecimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estabelecimentos")
@RequiredArgsConstructor
@Slf4j
public class EstabelecimentoController {

    private final EstabelecimentoService service;

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> save(@RequestBody @Valid EstabelecimentoRequestDTO request) {
        log.info("Creating new estabelecimento");
        var response = service.save(request);
        log.info("Estabelecimento created successfully with ID: {}", response.id());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findAll() {
        log.info("Retrieving all estabelecimentos");
        var estabelecimentos = service.findAll();
        log.info("Retrieved {} estabelecimentos", estabelecimentos.size());
        return ResponseEntity.ok(estabelecimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> findById(@PathVariable UUID id) {
        log.info("Retrieving estabelecimento with ID: {}", id);
        var estabelecimento = service.findById(id);
        log.info("Estabelecimento retrieved successfully: {}", id);
        return ResponseEntity.ok(estabelecimento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid EstabelecimentoRequestDTO request
    ) {
        log.info("Updating estabelecimento with ID: {}", id);
        var response = service.update(id, request);
        log.info("Estabelecimento updated successfully: {}", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Deleting estabelecimento with ID: {}", id);
        service.delete(id);
        log.info("Estabelecimento deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }
}

