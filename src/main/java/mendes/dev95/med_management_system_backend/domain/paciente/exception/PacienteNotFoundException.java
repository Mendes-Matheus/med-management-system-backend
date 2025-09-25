package mendes.dev95.med_management_system_backend.domain.paciente.exception;

import java.util.UUID;

public class PacienteNotFoundException extends  RuntimeException {
    public PacienteNotFoundException(UUID id) {
        super("Paciente not found with ID: " + id);
    }

    public PacienteNotFoundException(String message) {
        super(message);
    }

}
