package mendes.dev95.med_management_system_backend.domain.paciente.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mendes.dev95.med_management_system_backend.domain.procedimentopaciente.entity.ProcedimentoPaciente;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pacientes")
public class Paciente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(length = 15,   nullable = false)
    private String sexo;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(unique = true, length = 14, nullable = false)
    private String cpf;

    @Column(length = 20, nullable = false)
    private String rg;

    @Column(length = 20, unique = true, nullable = false)
    private String cns;

    @Column(length = 200,  nullable = false)
    private String nomePai;

    @Column(length = 200)
    private String nomeMae;

    @Column(length = 20,  nullable = false)
    private String telefone;

    @Column(name = "telefone_secundario", length = 20)
    private String telefoneSecundario;

    @Column(length = 100)
    private String email;

    @Column(length = 200, nullable = false)
    private String logradouro;

    @Column(length = 10, nullable = false)
    private String numero;

    @Column(length = 100, nullable = false)
    private String bairro;

    @Column(length = 1000)
    private String observacoes;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProcedimentoPaciente> procedimentos = new ArrayList<>();

}
