package demo.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    Integer id;

    @Column(name = "product_name", nullable = false, length = 100)
    String productName;

    @Lob
    @Column(name = "description")
    String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @Column(name = "image_url")
    String imageUrl;

    Boolean is_delete = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;
}