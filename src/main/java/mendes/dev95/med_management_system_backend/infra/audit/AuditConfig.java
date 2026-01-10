package mendes.dev95.med_management_system_backend.infra.audit;

import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.infra.security.AuthorizationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuração de auditoria automática para operações sensíveis
 * Registra automaticamente acessos a dados de pacientes (LGPD)
 */
@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy
public class AuditConfig {

    @Autowired
    private AuthorizationService authService;

    /**
     * Audita operações de leitura de pacientes
     */
    @AfterReturning(
            pointcut = "execution(* mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService.find*(..))",
            returning = "result"
    )
    public void auditPacienteRead(JoinPoint joinPoint, Object result) {
        if (result != null) {
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            log.info("AUDITORIA_READ - Método: {}, Usuário: {}, Args: {}",
                    methodName,
                    authService.getCurrentUser().getUsername(),
                    formatArgs(args)
            );
        }
    }

    /**
     * Audita operações de escrita/atualização
     */
    @Before("execution(* mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService.save(..)) || " +
            "execution(* mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService.update(..))")
    public void auditPacienteWrite(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("AUDITORIA_WRITE - Método: {}, Usuário: {}, Args: {}",
                methodName,
                authService.getCurrentUser().getUsername(),
                formatArgs(args)
        );
    }

    /**
     * Audita deleções (operação crítica)
     */
    @Before("execution(* mendes.dev95.med_management_system_backend.domain.paciente.service.PacienteService.delete(..))")
    public void auditPacienteDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        log.warn("AUDITORIA_DELETE - Usuário: {}, ID: {}",
                authService.getCurrentUser().getUsername(),
                args[0]
        );
    }

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");

            // Não logar objetos completos por questões de privacidade
            if (args[i] != null) {
                sb.append(args[i].getClass().getSimpleName());
            } else {
                sb.append("null");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}