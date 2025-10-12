package mendes.dev95.med_management_system_backend.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoIntegrityViolationException;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.exception.EstabelecimentoNotFoundException;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteAlreadyExistsException;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteIntegrityViolationException;
import mendes.dev95.med_management_system_backend.domain.paciente.exception.PacienteNotFoundException;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoAgendadoException;
import mendes.dev95.med_management_system_backend.domain.procedimento.exception.ProcedimentoNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("Validation error on {} [{}]: {}", request.getRequestURI(), correlationId, ex.getMessage());

        List<String> messages = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        String localizedMessage = getLocalizedMessage(
                                error.getDefaultMessage(),
                                error.getDefaultMessage()
                        );
                        return fieldError.getField() + ": " + localizedMessage;
                    }
                    return getLocalizedMessage(error.getDefaultMessage(), error.getDefaultMessage());
                })
                .collect(Collectors.toList());

        ErrorResponse response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                getLocalizedMessage("error.validation", "Validation Error"),
                messages,
                request,
                correlationId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("Constraint violation on {} [{}]: {}", request.getRequestURI(), correlationId, ex.getMessage());

        List<String> messages = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ErrorResponse response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                getLocalizedMessage("error.constraint.violation", "Constraint Violation"),
                messages,
                request,
                correlationId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        log.warn("Response status exception on {} [{}]: {} - {}",
                request.getRequestURI(), correlationId, status, ex.getReason());

        String reason = ex.getReason() != null ? ex.getReason() :
                getLocalizedMessage("error.unknown", "Unknown error");

        ErrorResponse response = buildErrorResponse(
                status,
                status.getReasonPhrase(),
                List.of(reason),
                request,
                correlationId
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.error("Data integrity violation on {} [{}]: {}",
                request.getRequestURI(), correlationId, ex.getMessage());

        String message = getLocalizedMessage("error.data.integrity", "Data integrity violation occurred");

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            message = getLocalizedMessage("error.duplicate.entry", "Duplicate entry or constraint violation");
        }

        ErrorResponse response = buildErrorResponse(
                HttpStatus.CONFLICT,
                getLocalizedMessage("error.conflict", "Conflict"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("Invalid request body on {} [{}]: {}",
                request.getRequestURI(), correlationId, ex.getMessage());

        String message = getLocalizedMessage("error.invalid.request.body", "Invalid request body format");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                getLocalizedMessage("error.bad.request", "Bad Request"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("Method not supported on {} [{}]: {}",
                request.getRequestURI(), correlationId, ex.getMessage());

        String message = getLocalizedMessage("error.method.not.supported",
                "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                getLocalizedMessage("error.method.not.allowed", "Method Not Allowed"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("Access denied on {} [{}]: {}",
                request.getRequestURI(), correlationId, ex.getMessage());

        String message = getLocalizedMessage("error.access.denied", "Access denied");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.FORBIDDEN,
                getLocalizedMessage("error.forbidden", "Forbidden"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.error("Unexpected error occurred on {} [{}]",
                request.getRequestURI(), correlationId, ex);

        String message = getLocalizedMessage("error.internal.server",
                "An unexpected error occurred. Please contact support with correlation ID: " + correlationId);

        ErrorResponse response = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getLocalizedMessage("error.internal.server.title", "Internal Server Error"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(EstabelecimentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEstabelecimentoNotFound(
            EstabelecimentoNotFoundException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("{} - Correlation ID: {}", ex.getMessage(), correlationId);

        String message = getLocalizedMessage("estabelecimento.notfound", "Estabelecimento não encontrado");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                getLocalizedMessage("error.not.found", "Not Found"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(PacienteAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePacienteAlreadyExists(
            PacienteAlreadyExistsException ex, HttpServletRequest request
    ){
        String correlationId = generateCorrelationId();
        log.warn("{} - Correlation ID: {}", ex.getMessage(), correlationId);

        String message = getLocalizedMessage("paciente.alreadyexists", "Paciente já cadastrado");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.CONFLICT,
                getLocalizedMessage("error.conflict", "Conflict"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EstabelecimentoIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleEstabelecimentoIntegrityViolation(
            EstabelecimentoIntegrityViolationException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.error("Estabelecimento integrity violation [{}]: {}", correlationId, ex.getMessage(), ex);

        String message = getLocalizedMessage("estabelecimento.delete.integrity.violation",
                "Não é possível excluir o estabelecimento devido a restrições de integridade");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.CONFLICT,
                getLocalizedMessage("error.conflict", "Conflict"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PacienteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePacienteNotFound(
            PacienteNotFoundException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("{} - Correlation ID: {}", ex.getMessage(), correlationId);

        String message = getLocalizedMessage("paciente.notfound", "Paciente não encontrado");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                getLocalizedMessage("error.not.found", "Not Found"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(PacienteIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handlePacienteIntegrityViolation(
            PacienteIntegrityViolationException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.error("Paciente integrity violation [{}]: {}", correlationId, ex.getMessage(), ex);

        String message = getLocalizedMessage("paciente.delete.integrity.violation",
                "Não é possível excluir o paciente devido a restrições de integridade");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.CONFLICT,
                getLocalizedMessage("error.conflict", "Conflict"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ProcedimentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProcedimentoNotFound(
            ProcedimentoNotFoundException ex, HttpServletRequest request) {

        String correlationId = generateCorrelationId();
        log.warn("{} - Correlation ID: {}", ex.getMessage(), correlationId);

        String message = getLocalizedMessage("procedimento.notfound", "Procedimento não encontrado");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                getLocalizedMessage("error.not.found", "Not Found"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProcedimentoAgendadoException.class)
    public ResponseEntity<ErrorResponse> handleProcedimentoAgendado(
            ProcedimentoAgendadoException  ex, HttpServletRequest request
    ){
        String correlationId = generateCorrelationId();
        log.warn("{} - Correlation ID: {}", ex.getMessage(), correlationId);

        String message = getLocalizedMessage("procedimento.agendado", "Paciente já possui agendamento para este procedimento");

        ErrorResponse response = buildErrorResponse(
                HttpStatus.CONFLICT,
                getLocalizedMessage("error.conflict", "Conflict"),
                List.of(message),
                request,
                correlationId
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    private String getLocalizedMessage(String code, String defaultMessage) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(code, null, locale);
        } catch (Exception ex) {
            log.debug("Message not found for code: {}, using default", code);
            return defaultMessage;
        }
    }

    private String generateCorrelationId() {
        return "ERR-" + System.currentTimeMillis() + "-" +
                ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String error,
            List<String> messages,
            HttpServletRequest request,
            String correlationId
    ) {
        return new ErrorResponse(
                status.value(),
                error,
                messages,
                LocalDateTime.now(),
                request.getRequestURI(),
                correlationId
        );
    }

}
