package mendes.dev95.med_management_system_backend.infra.external.cpf;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CpfApiService {

    @Value("${cpf.api.key}")
    private String apiKey;

    private final WebClient cpfWebClient;

    public ConsultaCpfApiResponse consultar(String cpf) {
        return cpfWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/consulta")
                        .queryParam("cpf", cpf)
                        .build())
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToMono(ConsultaCpfApiResponse.class)
                .block();
    }
}


