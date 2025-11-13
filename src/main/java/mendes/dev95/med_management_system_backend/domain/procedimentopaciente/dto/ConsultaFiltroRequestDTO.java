package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.StatusProcedimento;

import java.time.LocalDate;
import java.util.UUID;

public record ConsultaFiltroRequestDTO(

        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
        String cpf,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicio,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFim,

        StatusProcedimento status,

        UUID procedimentoId

) {
    public ConsultaFiltroRequestDTO {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser após data fim");
        }
    }
}
