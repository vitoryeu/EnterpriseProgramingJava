package ua.edu.lab7.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private Order order;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal lineAmount;

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order o) {
        this.order = o;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product p) {
        this.product = p;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer q) {
        this.quantity = q;
    }

    public BigDecimal getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(BigDecimal a) {
        this.lineAmount = a;
    }
}
