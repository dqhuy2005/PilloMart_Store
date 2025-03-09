package demo.restfulapi.service;

import demo.restfulapi.dto.request.ProductRequest;
import demo.restfulapi.dto.response.ProductResponse;
import demo.restfulapi.entity.Category;
import demo.restfulapi.entity.Product;
import demo.restfulapi.repository.CategoryRepository;
import demo.restfulapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::new).collect(Collectors.toList());
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("This product does not found!"));
    }

    public Page<ProductResponse> getProductByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Trả về ProductRepsonse
        return productRepository.findByCategoryCategoryNameContaining(name, pageable).map(ProductResponse::new);
    }

    public List<ProductResponse> getProductsByCategory(String name) {
        List<Product> products;

        if (name == null)
            products = productRepository.findAll();
        else
            products = productRepository.findByCategoryName(name);

        // Trả về ProductRepsonse
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public Page<ProductResponse> getProductsWithPagination(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductResponse::new);
    }

    public ProductResponse saveProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Category not found!"));

        Product product = new Product();

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());
        product.setIs_delete(false);

        return new ProductResponse(productRepository.save(product));
    }

}
