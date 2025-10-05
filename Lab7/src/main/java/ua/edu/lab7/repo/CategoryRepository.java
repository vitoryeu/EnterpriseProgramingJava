package ua.edu.lab7.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.lab7.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}