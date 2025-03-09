package demo.restfulapi.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import demo.restfulapi.dto.request.AuthRequest;
import demo.restfulapi.dto.response.AuthResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.repository.AccountRepository;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthResponse checkUser(AuthRequest request) {
        Account acc = accountRepository.findAccountByUsername(request.getUsername());

        if (acc != null && acc.getIs_active()) {
            var token = generatedToken(acc);
            return AuthResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        }

        if (!acc.getIs_active()) {
            return AuthResponse.builder()
                    .token("This account doesn't active anymore!!!")
                    .authenticated(false)
                    .build();
        }

        System.out.println("Created token failed!");
        return AuthResponse.builder()
                .token("Empty Token!!!")
                .authenticated(false)
                .build();

    }

    // JWT (header - payload - signer)
    public String generatedToken(Account account) {
        // header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("non4m")
                .claim("username", account.getUsername())
                .claim("role", account.getIs_admin())
                .claim("userId", account.getUserId())
                .issueTime(new Date())
                //.expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000)) // Token valid for 1 hour
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();

        // save các claim vào payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // add field token
        JWSObject jwsObject = new JWSObject(header, payload);

        // sign token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
