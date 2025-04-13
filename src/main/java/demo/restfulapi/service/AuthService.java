package demo.restfulapi.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import demo.restfulapi.dto.request.AuthRequest;
import demo.restfulapi.dto.response.AuthResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${jwt.expiration:3600}") // Default 1 hour if not specified
    private long tokenExpirationSeconds;


    // Các exceptions tùy chỉnh
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    public static class InactiveAccountException extends RuntimeException {
        public InactiveAccountException(String message) {
            super(message);
        }
    }

    public AuthResponse checkUser(AuthRequest request) {
        Account account = accountRepository.findAccountByUsername(request.getUsername());

        if (account == null) {
            logger.warn("Authentication failed: User '{}' not found", request.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        if (!request.getPassword().equals(account.getPassword())) {
            logger.warn("Authentication failed: Invalid password for user '{}'", request.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }

        if (!account.getIs_active()) {
            logger.warn("Authentication failed: Account '{}' is inactive", request.getUsername());
            throw new InactiveAccountException("Account is banned");
        }

        try {
            String token = generateToken(account);
            return AuthResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            logger.error("Token generation failed", e);
            throw new RuntimeException("Authentication failed due to server error", e);
        }
    }

    public String generateToken(Account account) {
        try {
            // Tạo JWT ID duy nhất
            //String jwtId = UUID.randomUUID().toString();

            // Header
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                    //.type("JWT")
                    .build();

            // Payload với thêm nhiều thông tin hữu ích
            Instant now = Instant.now();
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(account.getUsername())
                    .issuer("non4m")
                    //.jwtID(jwtId) // Thêm JWT ID để theo dõi
                    .claim("username", account.getUsername())
                    .claim("role", account.getIs_admin())
                    .issueTime(Date.from(now))
                    .notBeforeTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(tokenExpirationSeconds)))
                    .build();

            // Add payload
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());

            // Tạo JWS object
            JWSObject jwsObject = new JWSObject(header, payload);

            // Sign token
            jwsObject.sign(new MACSigner(signerKey.getBytes(StandardCharsets.UTF_8)));

            // Lưu thông tin token vào database hoặc cache (tùy chọn)
            // saveTokenInfo(jwtId, account.getUserId(), jwtClaimsSet.getExpirationTime());

            return jwsObject.serialize();
        } catch (JOSEException e) {
            logger.error("Failed to sign JWT", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    // Thêm các phương thức hỗ trợ khác
    public boolean validateToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes(StandardCharsets.UTF_8));

            if (!jwsObject.verify(verifier)) {
                return false;
            }

            // Kiểm tra thời gian hết hạn
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            Date expirationTime = claimsSet.getExpirationTime();

            return expirationTime != null && expirationTime.after(new Date());
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false;
        }
    }

    // Phương thức để thu hồi token (blacklist)
    public void revokeToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            String jwtId = claimsSet.getJWTID();

            // Thêm jwtId vào blacklist trong database hoặc cache
            // tokenBlacklistService.addToBlacklist(jwtId, claimsSet.getExpirationTime());
        } catch (Exception e) {
            logger.error("Token revocation failed", e);
            throw new RuntimeException("Failed to revoke token", e);
        }
    }
}