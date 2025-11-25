package mendes.dev95.med_management_system_backend.infra.external.cpf;

public class CpfApiException extends RuntimeException {
    public CpfApiException(String message) {
        super(message);
    }

    public CpfApiException(String message, Throwable cause) {
        super(message, cause);
    }
}