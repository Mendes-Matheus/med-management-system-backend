package mendes.dev95.med_management_system_backend.infra.security.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,  nullable = false, length = 1000)
    private String token;
    private String username;
    private Instant issuedAt;
    private Instant expiresAt;
    private boolean revoked;
}

