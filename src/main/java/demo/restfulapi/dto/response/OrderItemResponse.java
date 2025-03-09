package demo.restfulapi.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Integer productId;
    String productName;
    BigDecimal productPrice;
    Integer productQuantity;
    BigDecimal totalPrice;
}
