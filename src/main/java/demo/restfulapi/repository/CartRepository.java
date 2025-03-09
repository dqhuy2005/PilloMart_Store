package demo.restfulapi.repository;

import demo.restfulapi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(value = "SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> selectCartByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "DELETE FROM Cart c WHERE c.user.userId = :userId")
    void deleteCartWithUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT c FROM Cart c WHERE c.user.userId = :userId AND c.product.id = :productId")
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
