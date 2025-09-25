package mendes.dev95.med_management_system_backend.domain.paciente.exception;


public class PacienteAlreadyExistsException extends RuntimeException {
    public PacienteAlreadyExistsException() {
        super("Paciente already exists");
    }

    public PacienteAlreadyExistsException(String message) {
        super(message);
    }
}
