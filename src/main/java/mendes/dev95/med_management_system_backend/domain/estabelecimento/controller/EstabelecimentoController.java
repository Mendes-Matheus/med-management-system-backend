package mendes.dev95.med_management_system_backend.domain.estabelecimento.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.dto.EstabelecimentoResponseDTO;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.mapper.EstabelecimentoMapper;
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
public class EstabelecimentoController {

    private final EstabelecimentoService service;
    private final EstabelecimentoMapper mapper;

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> save(@RequestBody @Valid EstabelecimentoRequestDTO request) {
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
    public ResponseEntity<List<EstabelecimentoResponseDTO>> findAll() {
        var find = service.findAll();
        var response = mapper.toResponseList(find);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> findById(@PathVariable UUID id) {
        var find = service.findById(id);
        return ResponseEntity.ok(mapper.toResponse(find));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid EstabelecimentoRequestDTO request) {
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
