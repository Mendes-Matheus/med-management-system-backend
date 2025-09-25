package mendes.dev95.med_management_system_backend.domain.estabelecimento.exception;

import java.util.UUID;

public class EstabelecimentoIntegrityViolationException extends RuntimeException {
    public EstabelecimentoIntegrityViolationException(UUID id, String operation) {
        super("Integrity violation for estabelecimento ID: " + id + " during operation: " + operation);
    }

    public EstabelecimentoIntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
