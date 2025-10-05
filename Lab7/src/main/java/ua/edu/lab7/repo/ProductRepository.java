package ua.edu.lab7.repo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.edu.lab7.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select p from Product p where p.category.id=:categoryId and p.price between :min and :max")
    List<Product> findByCategoryAndPriceBetween(Long categoryId, BigDecimal min, BigDecimal max);
}
