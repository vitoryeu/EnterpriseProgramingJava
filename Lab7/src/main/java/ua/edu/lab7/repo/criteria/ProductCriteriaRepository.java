package ua.edu.lab7.repo.criteria;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ua.edu.lab7.model.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class ProductCriteriaRepository {
    private final EntityManager em;

    public ProductCriteriaRepository(EntityManager em) {
        this.em = em;
    }

    public List<Product> findByNameLikeAndPrice(String nameLike, BigDecimal min, BigDecimal max) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        Predicate p = cb.conjunction();
        if (nameLike != null && !nameLike.isBlank())
            p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + nameLike.toLowerCase() + "%"));
        if (min != null) p = cb.and(p, cb.ge(root.get("price"), min));
        if (max != null) p = cb.and(p, cb.le(root.get("price"), max));
        cq.where(p).orderBy(cb.asc(root.get("name")));
        return em.createQuery(cq).getResultList();
    }
}
