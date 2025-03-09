package demo.restfulapi.controller;

import demo.restfulapi.dto.request.CartRequest;
import demo.restfulapi.dto.response.CartResponse;
import demo.restfulapi.dto.response.InvoiceResponse;
import demo.restfulapi.entity.Order;
import demo.restfulapi.repository.OrderRepository;
import demo.restfulapi.service.CartService;
import demo.restfulapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestHeader("Authorization") String token, @RequestBody CartRequest request) throws ParseException {
        Integer userId = jwtUtil.existsUserId(token);
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @GetMapping("")
    public ResponseEntity<List<CartResponse>> getCart(@RequestHeader("Authorization") String token) throws ParseException {
        Integer userId = jwtUtil.existsUserId(token);
        return ResponseEntity.ok(cartService.getCartById(userId));
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> payment(@RequestHeader("Authorization") String token,
                                                       @RequestBody Map<String, String> request) throws ParseException {
        String address = request.get("address");
        Integer userId = jwtUtil.existsUserId(token);
        Integer orderId = cartService.checkOut(userId, address);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment was successful!");
        response.put("orderId", orderId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<InvoiceResponse> getCartByOrderId(@PathVariable("orderId") Integer orderId) throws ParseException {
        Order order = orderRepository.findById(orderId).orElse(null);
        System.out.println(orderId);
        if (order == null) return ResponseEntity.notFound().build();
        InvoiceResponse response = cartService.getInvoice(order);
        return ResponseEntity.ok(response);
    }

}
