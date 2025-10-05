package ua.edu.lab7.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.lab7.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}