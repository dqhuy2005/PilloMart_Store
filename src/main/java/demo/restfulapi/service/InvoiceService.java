package demo.restfulapi.service;

import demo.restfulapi.dto.response.InvoiceResponse;
import demo.restfulapi.dto.response.OrderItemResponse;
import demo.restfulapi.entity.Account;
import demo.restfulapi.entity.Order;
import demo.restfulapi.entity.OrderDetail;
import demo.restfulapi.repository.AccountRepository;
import demo.restfulapi.repository.OrderDetailRepository;
import demo.restfulapi.repository.OrderRepository;
import demo.restfulapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    AccountRepository accountRepository;


    /* InvoiceResponse:
        Integer orderId;
        Instant orderDate;
        String address;

        String userFullname;

        List<OrderItemResponse> orderItems;
        BigDecimal totalInvoice;
    */

    public List<InvoiceResponse> selectAllInvoices() {

        List<Order> orders = orderRepository.findAll();
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        for (Order order : orders) {
            InvoiceResponse invoiceResponse = new InvoiceResponse();
            invoiceResponse.setOrderId(order.getId());
            invoiceResponse.setOrderDate(order.getOrderDate());
            invoiceResponse.setAddress(order.getAddress());

            // userfullname
            Account account = accountRepository.findById(order.getUser().getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            invoiceResponse.setUserFullname(account.getFullname());


            List<OrderItemResponse> orderItems = new ArrayList<>();
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());

            BigDecimal totalPrice = BigDecimal.ZERO;

            for (OrderDetail orderDetail : orderDetails) {

                OrderItemResponse item = new OrderItemResponse();
                item.setProductPrice(orderDetail.getProduct().getPrice());
                item.setProductQuantity(orderDetail.getQuantity());
                item.setProductId(orderDetail.getProduct().getId());
                item.setProductName(orderDetail.getProduct().getProductName());
                item.setTotalPrice(orderDetail.getProduct().getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));

                orderItems.add(item);
                totalPrice = totalPrice.add(totalPrice);
            }

            invoiceResponse.setOrderItems(orderItems);
            invoiceResponse.setTotalInvoice(totalPrice);
            invoiceResponses.add(invoiceResponse);
        }
        return invoiceResponses;
    }

}
