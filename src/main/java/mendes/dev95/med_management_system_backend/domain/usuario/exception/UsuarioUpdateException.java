package mendes.dev95.med_management_system_backend.domain.usuario.exception;

public class UsuarioUpdateException extends RuntimeException {
    public UsuarioUpdateException(String message) {
        super(message);
    }

    public UsuarioUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
