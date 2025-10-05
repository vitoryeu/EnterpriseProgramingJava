package ua.edu.lab7.spec;

import org.springframework.data.jpa.domain.Specification;
import ua.edu.lab7.model.Product;

import java.math.BigDecimal;

public class ProductSpecs {
    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, q, cb) -> categoryId == null ? cb.conjunction() : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceGte(BigDecimal min) {
        return (root, q, cb) -> min == null ? cb.conjunction() : cb.ge(root.get("price"), min);
    }

    public static Specification<Product> priceLte(BigDecimal max) {
        return (root, q, cb) -> max == null ? cb.conjunction() : cb.le(root.get("price"), max);
    }
}
