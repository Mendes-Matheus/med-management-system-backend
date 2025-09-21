package mendes.dev95.med_management_system_backend.domain.procedimento.service;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.repository.EstabelecimentoRepository;
import mendes.dev95.med_management_system_backend.domain.procedimento.dto.ProcedimentoRequestDTO;
import mendes.dev95.med_management_system_backend.domain.procedimento.entity.Procedimento;
import mendes.dev95.med_management_system_backend.domain.procedimento.mapper.ProcedimentoMapper;
import mendes.dev95.med_management_system_backend.domain.procedimento.repository.ProcedimentoRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
@Log4j2
@RequiredArgsConstructor
public class ProcedimentoService {
    private final ProcedimentoRepository repository;
    private final MessageSource messageSource;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final ProcedimentoMapper mapper;


    public Procedimento save(Procedimento procedimento) {
        var estabelecimento = estabelecimentoRepository.findById(procedimento.getEstabelecimento().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("estabelecimento.notfound")
                ));

        var procedimentoToSave = Procedimento.builder()
                .id(procedimento.getId())
                .estabelecimento(estabelecimento)
                .nomeProcedimento(procedimento.getNomeProcedimento())
                .tipoProcedimento(procedimento.getTipoProcedimento())
                .observacoes(procedimento.getObservacoes())
                .orientacoes(procedimento.getOrientacoes())
                .build();

        return repository.save(procedimentoToSave);
    }

    public List<Procedimento> findAll() {
        return repository.findAll();
    }

    public Procedimento findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, getMessage("procedimento.notfound")
                ));
    }

    public Procedimento update(UUID id, ProcedimentoRequestDTO dto) {
        Procedimento procedimento = findById(id);

        mapper.updateEntityFromDto(dto, procedimento);

        if (dto.estabelecimentoId() == null) {
            var estabelecimento = estabelecimentoRepository.findById(procedimento.getEstabelecimento().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, getMessage("estabelecimento.notfound")
                    ));
            procedimento.setEstabelecimento(estabelecimento);
        }

        return repository.save(procedimento);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, getMessage("procedimento.notfound"));
        }
        repository.deleteById(id);
    }

    /**
     * Metodo auxiliar para buscar mensagens internacionalizadas
     */
    private String getMessage(@NonNull String code, @NonNull Object... args) {
        Locale locale = LocaleContextHolder.getLocale(); // detecta locale do request
        return messageSource.getMessage(code, args, locale);
    }

}
