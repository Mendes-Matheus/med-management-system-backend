package mendes.dev95.med_management_system_backend.domain.procedimento.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mendes.dev95.med_management_system_backend.domain.estabelecimento.entity.Estabelecimento;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "procedimentos")
public class Procedimento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private Long version;

    @Column(name = "nome_procedimento", length = 100, nullable = false)
    private String nomeProcedimento;

    @Column(name = "tipo_procedimento", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProcedimento tipoProcedimento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "procedimento_estabelecimento",
            joinColumns = @JoinColumn(name = "procedimento_id"),
            inverseJoinColumns = @JoinColumn(name = "estabelecimento_id")
    )
    @Builder.Default
    private List<Estabelecimento> estabelecimentos = new ArrayList<>();

    @OneToMany(mappedBy = "procedimento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private List<ProcedimentoPaciente> procedimentoPaciente = new ArrayList<>();

    @Column(length = 1000)
    private String observacoes;

    @Column(length = 1000)
    private String orientacoes;

}
