package demo.restfulapi.service;

import demo.restfulapi.dto.request.CartRequest;
import demo.restfulapi.dto.response.CartResponse;
import demo.restfulapi.dto.response.InvoiceResponse;
import demo.restfulapi.dto.response.OrderItemResponse;
import demo.restfulapi.entity.*;
import demo.restfulapi.repository.*;
import demo.restfulapi.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setProductId(cart.getProduct().getId());
        response.setProductImage(cart.getProduct().getImageUrl());
        response.setProductName(cart.getProduct().getProductName());
        response.setProductQuantity(cart.getQuantity());
        response.setPrice(cart.getPrice());

        return response;
    }

    public InvoiceResponse getInvoice(Order order) {
        InvoiceResponse response = new InvoiceResponse();

        /*   Get attributes from Order and OrderDetail   */

        // Order
        response.setOrderId(order.getId());
        response.setAddress(order.getAddress());
        response.setOrderDate(order.getOrderDate());
        response.setUserFullname(order.getUser().getFullname());

        BigDecimal total = BigDecimal.ZERO;

        // OrderDetail
        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            OrderItemResponse item = new OrderItemResponse();

            item.setProductId(orderDetail.getProduct().getId());
            item.setProductName(orderDetail.getProduct().getProductName());
            item.setProductPrice(orderDetail.getPrice());
            item.setProductQuantity(orderDetail.getQuantity());
            item.setTotalPrice(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));

            items.add(item);
            total = total.add(item.getTotalPrice());
        }
        response.setTotalInvoice(total);
        response.setOrderItems(items);

        return response;
    }

    @Transactional
    public CartResponse addToCart(Integer userId, CartRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found."));
        Account account = accountRepository.findById(userId).orElseThrow(() -> new RuntimeException("Account not found."));

        // Đã có sẵn trong giỏ hàng
        Cart existsCart = cartRepository.selectCartByUserIdAndProductId(userId, product.getId());
        if (existsCart != null) {
            existsCart.setQuantity(existsCart.getQuantity() + request.getQuantity());
            cartRepository.save(existsCart);
            return mapToCartResponse(existsCart);
        }

        Cart cart = new Cart();
        cart.setUser(account);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        cart.setPrice(request.getPrice());
        cart.setCreatedAt(new Date().toInstant());

        cartRepository.save(cart);

        // Cart -> CartResponse
        return mapToCartResponse(cart);
    }

    public List<CartResponse> getCartById(Integer userId) {
        List<Cart> carts = cartRepository.selectCartByUserId(userId);

        // List<Cart> -> List<CartResponse>
        return carts.stream().map(this::mapToCartResponse).collect(Collectors.toList());
    }

    public void deleteCart(Integer userId) {
        cartRepository.deleteCartWithUserId(userId);
    }

    @Transactional
    public Integer checkOut(Integer userId, String address) {
        List<Cart> carts = cartRepository.selectCartByUserId(userId);
        if (carts.isEmpty()) {
            throw new RuntimeException("Cart not found!");
        } else {
            Account account = accountRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found!"));

            // Add new order
            Order order = new Order();
            order.setUser(account);
            order.setAddress(address);
            order.setOrderDate(new Date().toInstant());

            orderRepository.save(order);

            // Add attributes của order_details
            for (Cart cart : carts) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cart.getProduct());
                orderDetail.setQuantity(cart.getQuantity());
                orderDetail.setPrice(cart.getPrice());
                orderDetailRepository.save(orderDetail);
            }

            cartRepository.deleteCartWithUserId(userId);

            return order.getId();
        }
    }
}
