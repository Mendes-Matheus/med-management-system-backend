package mendes.dev95.med_management_system_backend.domain.estabelecimento.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "estabelecimentos")
public class Estabelecimento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_estabelecimento", nullable = false, length = 200)
    private String nomeEstabelecimento;

    @Column(length = 1000)
    private String observacoes;

}
