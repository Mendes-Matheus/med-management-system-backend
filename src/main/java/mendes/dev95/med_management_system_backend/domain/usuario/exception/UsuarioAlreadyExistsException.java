package mendes.dev95.med_management_system_backend.domain.usuario.exception;

public class UsuarioAlreadyExistsException extends RuntimeException {
    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }

    public UsuarioAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
