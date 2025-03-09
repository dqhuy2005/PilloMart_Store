package demo.restfulapi.controller;

import demo.restfulapi.dto.request.AccountRequest;
import demo.restfulapi.dto.request.AuthRequest;
import demo.restfulapi.dto.response.AccountResponse;
import demo.restfulapi.dto.response.AuthResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.service.AccountService;
import demo.restfulapi.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.checkUser(request);
        if (response.getAuthenticated()) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody AccountRequest request) {
        Optional<Account> account = accountService.getAccountByUsername(request.getUsername());

        if (account.isPresent())
            return ResponseEntity.badRequest().body(AccountResponse.builder()
                    .username("Tên đăng nhập đã tồn tại.")
                    .build());
        else
            accountService.createAccount(request);

        return ResponseEntity.ok(AccountResponse.builder().build());
    }
}
