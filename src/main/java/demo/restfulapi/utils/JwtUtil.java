package demo.restfulapi.utils;

import com.nimbusds.jose.JWSObject;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JwtUtil {

    public Integer existsUserId(String token) throws ParseException {
        token = token.replace("Bearer ", "");

        // Parse token from frontend
        JWSObject jwsObject = JWSObject.parse(token);
        String payload = jwsObject.getPayload().toString();
        System.out.println("payload: " + payload);

        return Integer.parseInt(payload.split("\"userId\":")[1].split(",")[0]);
    }

}
