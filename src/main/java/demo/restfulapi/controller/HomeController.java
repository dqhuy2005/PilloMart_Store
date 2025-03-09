package demo.restfulapi.controller;

import demo.restfulapi.dto.response.ProductResponse;
import demo.restfulapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getData() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

}
