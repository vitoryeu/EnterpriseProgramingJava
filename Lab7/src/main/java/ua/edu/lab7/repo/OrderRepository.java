package ua.edu.lab7.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.lab7.model.Order;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.customer.id = :customerId " +
            "and o.createdAt between :from and :to")
    List<Order> findByCustomerAndPeriod(Long customerId, Instant from, Instant to);

    @Query("select p.category.name, sum(oi.lineAmount) " +
            "from Order o " +
            "join o.items oi " +
            "join oi.product p " +
            "group by p.category.name")
    List<Object[]> salesByCategory();
}