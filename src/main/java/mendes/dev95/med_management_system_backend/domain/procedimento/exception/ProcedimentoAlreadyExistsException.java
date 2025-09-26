package mendes.dev95.med_management_system_backend.domain.procedimento.exception;


public class ProcedimentoAlreadyExistsException extends RuntimeException {

    public ProcedimentoAlreadyExistsException() {
        super("Paciente already exists");
    }

    public ProcedimentoAlreadyExistsException(String message) {
        super(message);
    }

}
