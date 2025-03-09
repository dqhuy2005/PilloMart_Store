package demo.restfulapi.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    Integer orderId;
    Instant orderDate;
    String userFullname;
    String address;
    List<OrderItemResponse> orderItems;
    BigDecimal totalInvoice;
}
