package mendes.dev95.med_management_system_backend.domain.usuario.exception;

public class UsuarioFetchException extends RuntimeException {
    public UsuarioFetchException(String message) {
        super(message);
    }

    public UsuarioFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
