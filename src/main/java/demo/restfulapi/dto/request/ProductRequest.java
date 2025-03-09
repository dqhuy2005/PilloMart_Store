package demo.restfulapi.dto.request;

import demo.restfulapi.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    Integer id;
    String productName;
    String description;
    BigDecimal price;
    String imageUrl;
    Integer categoryId;
    String categoryName;

    public ProductRequest(Product product) {

        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getCategoryId();
        this.categoryName = product.getCategory().getCategoryName();

    }
}
