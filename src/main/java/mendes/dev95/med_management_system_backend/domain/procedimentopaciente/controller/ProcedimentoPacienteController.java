package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto.*;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.service.ProcedimentoPacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/consultas")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findAllConsultas(
            @PageableDefault(size = 10, sort = "dataSolicitacao", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAllConsultas(pageable));
    }

    @GetMapping("/exames")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findAllExames(
            @PageableDefault(
                    size = 10, sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAllExames(pageable));
    }

    @GetMapping("/cirurgias")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findAllCirurgias(
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAllCirurgias(pageable));
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
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findByProcedimentoId(
            @PathVariable UUID id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByProcedimentoId(id, pageable));
    }

    @GetMapping("/consultas/procedimento/{id}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findConsultasByProcedimentoId(
            @PathVariable UUID id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findConsultasByProcedimentoId(id, pageable));
    }

    @GetMapping("/exames/procedimento/{id}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findExamesByProcedimentoId(
            @PathVariable UUID id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findExamesByProcedimentoId(id, pageable));
    }

    @GetMapping("/cirurgias/procedimento/{id}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findCirurgiasByProcedimentoId(
            @PathVariable UUID id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findCirurgiasByProcedimentoId(id, pageable));
    }

    @GetMapping("/procedimento/status/{status}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findByProcedimentoStatus(
            @PathVariable String status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByProcedimentoStatus(status, pageable));
    }

    @GetMapping("/consultas/status/{status}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findConsultasByProcedimentoStatus(
            @PathVariable String status,
               @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findConsultasByProcedimentoStatus(status, pageable));
    }

    @GetMapping("/exames/status/{status}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findExamesByProcedimentoStatus(
            @PathVariable String status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findExamesByProcedimentoStatus(status, pageable));
    }

    @GetMapping("/cirurgias/status/{status}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findCirurgiasByProcedimentoStatus(
            @PathVariable String status,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findCirurgiasByProcedimentoStatus(status, pageable));
    }

    @GetMapping("/paciente/cpf/{cpf}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findByPacienteCpf(
            @PathVariable String cpf,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByPacienteCpf(cpf, pageable));
    }

    @GetMapping("/paciente/consultas/cpf/{cpf}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findConsultasByPacienteCpf(
            @PathVariable String cpf,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findConsultasByPacienteCpf(cpf, pageable));
    }

    @GetMapping("/paciente/exames/cpf/{cpf}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findExamesByPacienteCpf(
            @PathVariable String cpf,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findExamesByPacienteCpf(cpf, pageable));
    }

    @GetMapping("/paciente/cirurgias/cpf/{cpf}")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findCirurgiasByPacienteCpf(
            @PathVariable String cpf,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findCirurgiasByPacienteCpf(cpf, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoPacienteSimpleResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid ProcedimentoPacienteUpdateDTO request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/consultas/periodo")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findConsultasBetweenDates(
            @Valid ConsultaPeriodoRequest periodoRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findConsultasBetweenDates(
                periodoRequest.dataInicio(),
                periodoRequest.dataFim(),
                pageable
        ));
    }

    @GetMapping("exames/periodo")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findExamesBetweenDates(
            @Valid ConsultaPeriodoRequest periodoRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findExamesBetweenDates(
                periodoRequest.dataInicio(),
                periodoRequest.dataFim(),
                pageable
        ));
    }

    @GetMapping("cirurgias/periodo")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findCirurgiasBetweenDates(
            @Valid ConsultaPeriodoRequest periodoRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findCirurgiasBetweenDates(
                periodoRequest.dataInicio(),
                periodoRequest.dataFim(),
                pageable
        ));
    }

    @GetMapping("/consultas/filtro")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findConsultasByFiltro(
            @Valid ConsultaFiltroRequestDTO filtroRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findConsultasByFiltro(filtroRequest, pageable));
    }

    @GetMapping("/exames/filtro")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findExamesByFiltro(
            @Valid ConsultaFiltroRequestDTO filtroRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findExamesByFiltro(filtroRequest, pageable));
    }

    @GetMapping("/cirurgias/filtro")
    public ResponseEntity<Page<ProcedimentoPacienteSimpleResponseDTO>> findCirurgiasByFiltro(
            @Valid ConsultaFiltroRequestDTO filtroRequest,
            @PageableDefault(
                    size = 10,
                    sort = "dataSolicitacao",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findCirurgiasByFiltro(filtroRequest, pageable));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
