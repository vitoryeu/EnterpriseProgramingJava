package ua.edu.lab7.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.edu.lab7.model.*;
import ua.edu.lab7.service.ShopService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShopController {
    private final ShopService service;

    public ShopController(ShopService service) {
        this.service = service;
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category c) {
        return service.createCategory(c);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product p) {
        return service.createProduct(p);
    }

    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer c) {
        return service.createCustomer(c);
    }

    @GetMapping("/products")
    public Object listProducts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "sort", defaultValue = "id") String sort) {
        return service.listProducts(page, size, sort);
    }

    @GetMapping("/products/filter")
    public Object filterProducts(@RequestParam(name = "categoryId", required = false) Long categoryId, @RequestParam(name = "min", required = false) BigDecimal min, @RequestParam(name = "max", required = false) BigDecimal max, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return service.filterProducts(categoryId, min, max, page, size);
    }

    @GetMapping("/products/search")
    public Object criteriaSearch(@RequestParam(name = "q", required = false) String q, @RequestParam(name = "min", required = false) BigDecimal min, @RequestParam(name = "max", required = false) BigDecimal max) {
        return service.criteriaSearch(q, min, max);
    }

    @PostMapping("/orders/place/{customerId}")
    public Order place(@PathVariable("customerId") Long customerId, @RequestBody List<ShopService.ItemRequest> items) {
        return service.placeOrder(customerId, items);
    }

    @PostMapping("/orders/{orderId}/status")
    public Order updateStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") OrderStatus status) {
        return service.updateOrderStatus(orderId, status);
    }

    @PostMapping("/categories/{categoryId}/bulk-price")
    public ResponseEntity<?> bulk(@PathVariable("categoryId") Long categoryId, @RequestParam("factor") BigDecimal factor) {
        service.bulkUpdatePricesByCategory(categoryId, factor);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/orders/{orderId}/cancel")
    public Order cancel(@PathVariable("orderId") Long orderId) {
        return service.cancelOrder(orderId);
    }

    @GetMapping("/orders/by-customer")
    public Object ordersByCustomer(@RequestParam("customerId") Long customerId, @RequestParam("from") String from, @RequestParam("to") String to) {
        return service.findOrdersOfCustomer(customerId, Instant.parse(from), Instant.parse(to));
    }

    @GetMapping("/stats/sales-by-category")
    public List<Object[]> stats() {
        return service.salesByCategory();
    }
}
