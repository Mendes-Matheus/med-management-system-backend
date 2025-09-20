package mendes.dev95.med_management_system_backend.procedimentopaciente.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import mendes.dev95.med_management_system_backend.paciente.entity.Paciente;
import mendes.dev95.med_management_system_backend.procedimento.entity.Procedimento;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "procedimentos_paciente")
public class ProcedimentoPaciente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusProcedimento status;

    @Column(name = "data_solicitacao")
    private LocalDate dataSolicitacao;

    @Column(name = "data_agendamento")
    private LocalDate dataAgendamento;

    @Column(length = 1000)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", updatable = false)
    @JsonBackReference
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedimento_id")
    @JsonBackReference
    private Procedimento procedimento;

}
