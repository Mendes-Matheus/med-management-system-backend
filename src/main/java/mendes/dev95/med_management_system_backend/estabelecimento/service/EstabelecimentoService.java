package mendes.dev95.med_management_system_backend.estabelecimento.service;

import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.estabelecimento.dto.EstabelecimentoRequestDTO;
import mendes.dev95.med_management_system_backend.estabelecimento.entity.Estabelecimento;
import mendes.dev95.med_management_system_backend.estabelecimento.mapper.EstabelecimentoMapper;
import mendes.dev95.med_management_system_backend.estabelecimento.repository.EstabelecimentoRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository repository;
    private final MessageSource messageSource;
    private final EstabelecimentoMapper mapper;

    public Estabelecimento save(Estabelecimento estabelecimento) {
        return repository.save(estabelecimento);
    }

    public List<Estabelecimento> findAll() {
        return repository.findAll();
    }

    public Estabelecimento findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("estabelecimento.notfound")
                ));
    }

    public Estabelecimento update(UUID id, EstabelecimentoRequestDTO dto) {
        Estabelecimento estabelecimento = findById(id);

        mapper.updateEntityFromDto(dto, estabelecimento);

        return repository.save(estabelecimento);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getMessage("estabelecimento.delete.notfound"));
        }
        repository.deleteById(id);
    }

    /**
     * Metodo auxiliar para buscar mensagens internacionalizadas
     */
    private String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
