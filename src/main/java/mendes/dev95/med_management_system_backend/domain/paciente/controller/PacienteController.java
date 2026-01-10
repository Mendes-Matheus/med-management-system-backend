package mendes.dev95.med_management_system_backend.domain.paciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.paciente.dto.*;
import mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService;
import mendes.dev95.med_management_system_backend.infra.external.cpf.ConsultaCpfApiResponse;
import mendes.dev95.med_management_system_backend.infra.external.cpf.CpfApiResponse;
import mendes.dev95.med_management_system_backend.infra.external.cpf.CpfApiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;
    private final CpfApiService cpfApiService;


    @PostMapping
    public ResponseEntity<PacienteFullResponseDTO> save(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        var response = service.save(requestDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/cpf/api/{cpf}")
    public ResponseEntity<ConsultaCpfApiResponse> consultarApiCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(cpfApiService.consultar(cpf));
    }

//    @GetMapping("/consultar-cpf/{cpf}")
//    public ResponseEntity<CpfApiResponse> consultarCpf(@PathVariable String cpf) {
//
//        try {
//            CpfApiResponse response = service.consultarCpf(cpf);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }


    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<PacienteResponseDTO>> findByNome(
            @PathVariable String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.findByNome(nome, pageable));
    }


    @GetMapping("procedimentos/{id}")
    public ResponseEntity<PacienteResponseWithProcedimentosDTO> findProcedimentosPaciente(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findProcedimentosPaciente(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PacienteResponseDTO> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @GetMapping("/cns/{cns}")
    public ResponseEntity<PacienteResponseDTO> findByCns(@PathVariable String cns) {
        return ResponseEntity.ok(service.findByCns(cns));
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
