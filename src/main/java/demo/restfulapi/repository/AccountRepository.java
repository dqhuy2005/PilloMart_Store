package demo.restfulapi.repository;

import demo.restfulapi.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);

    Account findAccountByUsername(String username);

    Page<Account> findAccountsByUsername(String username, Pageable pageable);

    Page<Account> findAll(Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Account a SET a.is_active = false")
    void deleteAccount(@Param("userId") Integer userId);
}
