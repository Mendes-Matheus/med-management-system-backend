package mendes.dev95.med_management_system_backend.infra.util;

import lombok.NonNull;

public final class MaskUtil {

    private MaskUtil() {
        // Classe utilitária - evita instância
    }

    public static String maskEmail(@NonNull String email) {
        int at = email.indexOf("@");
        if (at <= 0) return "***"; // email inválido ou vazio

        String localPart = email.substring(0, at);
        String domainPart = email.substring(at);

        if (localPart.length() <= 3) {
            return "***" + domainPart;
        }

        int visibleLength = localPart.length() - 3;
        return localPart.substring(0, visibleLength) + "***" + domainPart;
    }

    public static String maskCpf(@NonNull String cpf) {
        if (cpf.length() < 11) return "***"; // CPF inválido

        String start = cpf.substring(0, 3);
        String end = cpf.substring(cpf.length() - 5);
        return start + "***" + end;
    }

    public static String maskPhone(@NonNull String phone) {
        if (phone.length() < 10) return "***"; // telefone inválido

        String start = phone.substring(0, 5);
        String end = phone.substring(phone.length() - 2);
        return start + "****" + end;
    }
}

