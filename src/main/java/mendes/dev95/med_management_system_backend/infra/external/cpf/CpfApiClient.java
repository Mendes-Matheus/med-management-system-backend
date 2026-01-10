package mendes.dev95.med_management_system_backend.infra.external.cpf;

import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.infra.util.MaskUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CpfApiClient {

//    private final WebClient webClient;
//    private final String apiKey;
//
//    public CpfApiClient(
//            WebClient.Builder webClientBuilder,
//            @Value("${cpf.api.url:https://apicpf.com/api}") String baseUrl,
//            @Value("${cpf.api.key}") String apiKey) {
//        this.webClient = webClientBuilder
//                .baseUrl(baseUrl)
//                .defaultHeader("X-API-KEY", apiKey)
//                .build();
//        this.apiKey = apiKey;
//    }
//
//    public CpfApiResponse consultarCpf(String cpf) {
//        log.debug("Consultando API de CPF para: {}", MaskUtil.maskCpf(cpf));
//
//        try {
//            return webClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/consulta")
//                            .queryParam("cpf", cpf)
//                            .build())
//                    .retrieve()
//                    .onStatus(status -> !status.is2xxSuccessful(),
//                            response -> handleErrorResponse(response.statusCode(), cpf))
//                    .bodyToMono(CpfApiResponse.class)
//                    .block();
//
//        } catch (WebClientResponseException e) {
//            log.error("Erro na chamada à API de CPF para {}: {}", MaskUtil.maskCpf(cpf), e.getMessage());
//            throw new CpfApiException("Erro ao consultar API de CPF: " + e.getStatusCode());
//        } catch (Exception e) {
//            log.error("Erro inesperado ao consultar API de CPF para {}: {}", MaskUtil.maskCpf(cpf), e.getMessage());
//            throw new CpfApiException("Erro inesperado ao consultar API de CPF");
//        }
//    }
//
//    private Mono<? extends Throwable> handleErrorResponse(HttpStatusCode status, String cpf) {
//        return Mono.error(new CpfApiException(
//                String.format("API de CPF retornou status %s para o CPF: %s",
//                        status.value(), MaskUtil.maskCpf(cpf))
//        ));
//    }
}