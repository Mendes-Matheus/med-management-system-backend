package mendes.dev95.med_management_system_backend.domain.procedimento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

public class ProcedimentoAgendadoException extends RuntimeException {

    public ProcedimentoAgendadoException() {
        super("O procedimento já está agendado para este paciente.");
    }

    public ProcedimentoAgendadoException(String message) {
        super(message);
    }
}
