package demo.restfulapi.repository;

import demo.restfulapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c.category_name FROM categories c", nativeQuery = true)
    List<Category> findAllCategoryName();

    Category findByCategoryId(Integer categoryId);

}
