package mendes.dev95.med_management_system_backend.domain.usuario.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioAuthResponseDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioLoginRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.dto.UsuarioRegisterRequestDTO;
import mendes.dev95.med_management_system_backend.domain.usuario.service.AuthService;
import mendes.dev95.med_management_system_backend.infra.security.service.JwtBlocklistService;
import mendes.dev95.med_management_system_backend.infra.security.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtBlocklistService blocklistService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioAuthResponseDTO> login(@Valid @RequestBody UsuarioLoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioAuthResponseDTO> register(@Valid @RequestBody UsuarioRegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "token_missing"));
        }

        String token = header.substring(7);
        String jti = authService.extractJti(token);
        long ttlMillis = authService.remainingValidityMillis(token);

        if (jti != null && ttlMillis > 0) {
            blocklistService.blockJti(jti, Duration.ofMillis(ttlMillis));
        }

        return ResponseEntity.ok(Map.of("status", "logged_out"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "missing_refresh"));
        }

        // Extrai username do refresh token
        String username = authService.extractUsername(refreshToken);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_token"));
        }

        // Valida token no Redis (token precisa estar igual ao armazenado)
        if (!refreshTokenService.validate(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid_refresh"));
        }

        // Revoga token antigo
        refreshTokenService.revoke(username);

        // Gera novo par de tokens
        UsuarioAuthResponseDTO newTokens = authService.generateTokens(username);

        // Armazena novo refresh no Redis
        refreshTokenService.create(username, newTokens.refreshToken(), newTokens.refreshTokenExpiration());

        return ResponseEntity.ok(newTokens);
    }

}
