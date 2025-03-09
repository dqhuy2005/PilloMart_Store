package demo.restfulapi.repository;

import demo.restfulapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product getProductById(Integer id);

    @Query(value = "SELECT * FROM products WHERE product_name LIKE %:keyword%", nativeQuery = true)
    List<Product> findByName(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :name")
    List<Product> findByCategoryName(@Param("name") String name);

    Page<Product> findByCategoryCategoryNameContaining(String name, Pageable pageable);

    Page<Product> findAll(Pageable pageable);



}
