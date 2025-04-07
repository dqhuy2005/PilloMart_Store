package demo.restfulapi.controller;

import demo.restfulapi.dto.request.ProductRequest;
import demo.restfulapi.dto.response.ProductResponse;
import demo.restfulapi.entity.Product;
import demo.restfulapi.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType) {

        Pageable pageable = PageRequest.of(page, size, sortType.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Page<ProductResponse> products = productService.getProductsWithPagination(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ProductResponse(product));
    }

    // /api/products/search?name=Gối
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProductsByName(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {

        log.info("Received request to search products by name: {}", name);
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Page<ProductResponse> products = productService.getProductByName(name, page, size);
        System.out.println(products.getContent());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductResponse>> searchProductsByCategory(@RequestParam String category) {
        if (category == null || category.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.saveProduct(productRequest));
    }

}
