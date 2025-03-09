package demo.restfulapi.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    Integer id;
    Integer productId;
    String productImage;
    String productName;
    Integer productQuantity;
    BigDecimal price;

}
