package demo.restfulapi.dto.response;


import demo.restfulapi.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer productId;
    String productName;
    String description;
    BigDecimal price;
    Integer categoryId;
    String categoryName;
    String imageUrl;
    Boolean isDelete;

    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryId = product.getCategory().getCategoryId();
        this.categoryName = product.getCategory().getCategoryName();
        this.imageUrl = product.getImageUrl();
        this.isDelete = product.getIs_delete();
    }
}
