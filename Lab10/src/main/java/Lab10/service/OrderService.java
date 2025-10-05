package Lab10.service;

import Lab10.domain.Order;
import Lab10.domain.OrderStatus;
import Lab10.dto.OrderDTO;
import Lab10.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order createOrder(OrderDTO dto) {
        Order order = new Order(dto.productId(), dto.productName(), dto.amount(), OrderStatus.NEW);
        return orderRepository.save(order);
    }

    @Transactional
    public void processPayment(Long orderId, double amount) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        try {
            paymentService.processPayment("order-" + order.getId(), amount);
            order.markPaid();
        } catch (InsufficientFundsException e) {
            order.markFailed();
            throw e;
        }
    }
}
