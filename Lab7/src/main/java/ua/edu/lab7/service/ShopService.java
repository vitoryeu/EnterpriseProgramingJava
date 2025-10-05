package ua.edu.lab7.service;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.lab7.model.*;
import ua.edu.lab7.repo.*;
import ua.edu.lab7.repo.criteria.ProductCriteriaRepository;
import ua.edu.lab7.spec.ProductSpecs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class ShopService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;
    private final ProductCriteriaRepository productCriteriaRepo;

    public ShopService(ProductRepository productRepo, CategoryRepository categoryRepo, CustomerRepository customerRepo, OrderRepository orderRepo, ProductCriteriaRepository productCriteriaRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
        this.productCriteriaRepo = productCriteriaRepo;
    }

    public Category createCategory(Category c) {
        return categoryRepo.save(c);
    }

    public Product createProduct(Product p) {
        return productRepo.save(p);
    }

    public Customer createCustomer(Customer c) {
        return customerRepo.save(c);
    }

    public Product getProduct(Long id) {
        return productRepo.findById(id).orElseThrow();
    }

    public Page<Product> listProducts(int page, int size, String sort) {
        return productRepo.findAll(PageRequest.of(page, size, Sort.by(sort)));
    }

    public List<Product> findProductsByCategoryAndPrice(Long categoryId, BigDecimal min, BigDecimal max) {
        return productRepo.findByCategoryAndPriceBetween(categoryId, min, max);
    }

    public Page<Product> filterProducts(Long categoryId, BigDecimal min, BigDecimal max, int page, int size) {
        Specification<Product> spec = Specification.where(ProductSpecs.hasCategory(categoryId)).and(ProductSpecs.priceGte(min)).and(ProductSpecs.priceLte(max));
        return productRepo.findAll(spec, PageRequest.of(page, size, Sort.by("price").ascending()));
    }

    public List<Product> criteriaSearch(String nameLike, BigDecimal min, BigDecimal max) {
        return productCriteriaRepo.findByNameLikeAndPrice(nameLike, min, max);
    }

    public List<Order> findOrdersOfCustomer(Long customerId, Instant from, Instant to) {
        return orderRepo.findByCustomerAndPeriod(customerId, from, to);
    }

    public List<Object[]> salesByCategory() {
        return orderRepo.salesByCategory();
    }

    @Transactional
    public Order placeOrder(Long customerId, List<ItemRequest> items) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);
        order.setTotalAmount(java.math.BigDecimal.ZERO);
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (ItemRequest ir : items) {
            Product p = productRepo.findById(ir.productId()).orElseThrow();
            var line = p.getPrice().multiply(java.math.BigDecimal.valueOf(ir.quantity()));
            OrderItem oi = new OrderItem();
            oi.setProduct(p);
            oi.setQuantity(ir.quantity());
            oi.setLineAmount(line);
            order.addItem(oi);
            total = total.add(line);
        }
        order.setTotalAmount(total);
        return orderRepo.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus(status);
        return order;
    }

    @Transactional
    public void bulkUpdatePricesByCategory(Long categoryId, java.math.BigDecimal factor) {
        var products = productRepo.findAll((root, q, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        for (var p : products) {
            p.setPrice(p.getPrice().multiply(factor));
        }
        productRepo.saveAll(products);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }

    public static record ItemRequest(Long productId, int quantity) {
    }
}
