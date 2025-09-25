package mendes.dev95.med_management_system_backend.domain.paciente.exception;

import java.util.UUID;

public class PacienteIntegrityViolationException extends RuntimeException {
    public PacienteIntegrityViolationException(UUID id, String operation) {
        super("Integrity violation for paciente with ID: " + id + " during operation: " + operation);
    }

    public PacienteIntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
