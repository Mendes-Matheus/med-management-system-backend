package mendes.dev95.med_management_system_backend.domain.usuario.exception;


public class UsuarioRegistrationException extends RuntimeException {
    public UsuarioRegistrationException(String message) {
        super(message);
    }

    public UsuarioRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
