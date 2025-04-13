package demo.restfulapi;

import demo.restfulapi.dto.request.AuthRequest;
import demo.restfulapi.dto.response.AuthResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.repository.AccountRepository;
import demo.restfulapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(authService, "SIGNER_KEY", "testSignerKey123456789012345678901234567890");
    }

    @Test(description = "Check account exists and verify again")
    public void testLoginSuccess() {

        // Request from client
        AuthRequest request = new AuthRequest("hung", "123");

        // Mock data
        Account mockAccount = new Account("hung", "123");

        when(accountRepository.findAccountByUsername(request.getUsername())).thenReturn(mockAccount);

        AuthResponse response = authService.checkUser(request);

        Assertions.assertTrue(response.getAuthenticated());
        Assertions.assertNotNull(response.getToken());

        verify(accountRepository).findAccountByUsername(request.getUsername());
    }

    @Test
    public void testLoginEndpoint() {
        // Test qua HTTP endpoint
        String url = "http://localhost:8080/api/login";

        AuthRequest request = new AuthRequest("hung", "123");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<AuthRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, AuthResponse.class);

        Assert.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().getAuthenticated());
        Assertions.assertNotNull(response.getBody().getToken());
    }

    @org.junit.jupiter.api.Test
    public void testLoginReturnsToken() {
        given()
                .baseUri("http://localhost:8080")
                .contentType("application/json")
                .body("{ \"username\": \"hung\", \"password\": \"123\" }")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

}
