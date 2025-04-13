package demo.restfulapi.dto.response;

import demo.restfulapi.entity.Account;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {

    Integer id;
    String username;
    String email;
    String fullname;
    Boolean role;

    public AccountResponse(Account account) {
        this.id = account.getUserId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.fullname = account.getFullname();
        this.role = account.getIs_admin();
    }

}
