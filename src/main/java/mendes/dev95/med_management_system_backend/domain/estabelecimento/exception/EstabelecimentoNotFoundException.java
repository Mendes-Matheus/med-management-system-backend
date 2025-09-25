package mendes.dev95.med_management_system_backend.domain.estabelecimento.exception;

import java.util.UUID;

public class EstabelecimentoNotFoundException extends RuntimeException {
    public EstabelecimentoNotFoundException(UUID id) {
        super("Estabelecimento not found with ID: " + id);
    }

    public EstabelecimentoNotFoundException(String message) {
        super(message);
    }
}
