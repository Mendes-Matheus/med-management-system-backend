package mendes.dev95.med_management_system_backend.infra.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        List<String> messages,
        LocalDateTime timestamp,
        String path,
        String correlationId  // Para rastreamento de logs
) {}
