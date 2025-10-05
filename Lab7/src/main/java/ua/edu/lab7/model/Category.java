package ua.edu.lab7.model;

import jakarta.persistence.*;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Category implements BaseEntity.Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;
    private Instant createdAt;
    private Instant updatedAt;

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category p) {
        this.parentCategory = p;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant t) {
        this.createdAt = t;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant t) {
        this.updatedAt = t;
    }
}
