package demo.restfulapi.controller;

import demo.restfulapi.dto.request.AccountRequest;
import demo.restfulapi.dto.response.AccountResponse;
import demo.restfulapi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType) {

        Pageable pageable = PageRequest.of(page, size, sortType.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<AccountResponse> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Integer userId, @RequestBody AccountRequest request) {
        System.out.println("Update user!");
        System.out.println("Received is_admin: " + request.getIs_admin());
        AccountResponse accountResponse = accountService.updateAccount(userId, request);
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer userId) {
        System.out.println("Delete user!");
        accountService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }

}
