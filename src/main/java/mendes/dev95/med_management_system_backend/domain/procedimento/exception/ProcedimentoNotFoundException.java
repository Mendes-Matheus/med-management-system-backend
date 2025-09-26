package mendes.dev95.med_management_system_backend.domain.procedimento.exception;

import java.util.UUID;

public class ProcedimentoNotFoundException extends RuntimeException {
    public ProcedimentoNotFoundException(UUID id) {
        super("Procedimento with id " + id + " not found.");
    }

    public ProcedimentoNotFoundException(String message) {
        super(message);
    }
}
