package demo.restfulapi.service;

import demo.restfulapi.dto.request.AccountRequest;
import demo.restfulapi.dto.response.AccountResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map(AccountResponse::new);
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).stream().findAny();
    }

    public Account createAccount(AccountRequest request) {
        Account account = new Account();

        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setFullname(request.getFullname());
        account.setEmail(request.getEmail());

        accountRepository.save(account);

        return account;
    }

    public AccountResponse updateAccount(Integer userId, AccountRequest request) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new RuntimeException("Account not found!"));

        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setIs_admin(request.getIs_admin());

        System.out.println("Role: " + account.getIs_admin());
        account.setFullname(request.getFullname());

        Account updatedAccount = accountRepository.save(account);

        return new AccountResponse(updatedAccount);
    }

    public void deleteAccount(Integer userId) {
        accountRepository.deleteById(userId);
        //accountRepository.deleteAccount(userId);
    }

}
