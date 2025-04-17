package demo.restfulapi;

import demo.restfulapi.dto.request.AuthRequest;
import demo.restfulapi.dto.response.AuthResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.repository.AccountRepository;
import demo.restfulapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void setup() {
        // Tạo user test trong DB
        Account testAccount = new Account();
        testAccount.setUsername("asd");
        testAccount.setPassword("123");
        testAccount.setIs_active(true);
        testAccount.setIs_delete(false);
        accountRepository.save(testAccount);
        ReflectionTestUtils.setField(authService, "signerKey", "testSignerKey123456789012345678901234567890");
    }

    @Test(description = "Check account exists and verify again")
    public void testLoginSuccess() {
        AuthRequest request = new AuthRequest("hung", "123");
        AuthResponse response = authService.checkUser(request);

        Assertions.assertTrue(response.getAuthenticated());
        Assertions.assertNotNull(response.getToken());
    }

    @Test
    public void testLoginEndpoint() {
        // Test qua HTTP endpoint
        String url = "http://localhost:8080/api/login";

        AuthRequest request = new AuthRequest("hung", "123");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(url, entity, AuthResponse.class);

        Assert.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().getAuthenticated());
        Assertions.assertNotNull(response.getBody().getToken());
    }

}
