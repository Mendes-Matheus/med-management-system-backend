package mendes.dev95.med_management_system_backend.domain.procedimentopaciente.exception;

public class ProcedimentoPacienteNotFoundException extends RuntimeException {

    public ProcedimentoPacienteNotFoundException(String message, Throwable cause) {
        super(message,  cause);
    }
}
