package demo.restfulapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    @Column(name = "username", nullable = false, length = 50)
    String username;

    @Column(name = "password", nullable = false, length = 255)
    String password;

    @Column(name = "fullname", nullable = false, length = 255)
    String fullname;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    String email;

    Boolean is_active = true;
    Boolean is_admin = false;
    Boolean is_delete = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Cart> carts;

    public Account(Integer userId) {
    }
}
