package Lab10.web;

import Lab10.domain.Order;
import Lab10.dto.OrderDTO;
import Lab10.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderDTO dto) {
        Order saved = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
