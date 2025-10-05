package Lab10.test;

import Lab10.domain.Order;
import Lab10.domain.OrderStatus;
import Lab10.dto.OrderDTO;
import Lab10.repository.OrderRepository;
import Lab10.service.InsufficientFundsException;
import Lab10.service.OrderService;
import Lab10.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock PaymentService paymentService;
    @InjectMocks OrderService orderService;

    @Test
    void createOrder_ValidData_Success() {
        OrderDTO dto = new OrderDTO("1", "Product", 100.0);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Order created = orderService.createOrder(dto);

        assertThat(created).isNotNull();
        assertThat(created.getStatus()).isEqualTo(OrderStatus.NEW);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processPayment_InsufficientFunds_ThrowsException() {
        Order order = new Order("1", "P", 50.0, OrderStatus.NEW);
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
        doThrow(new InsufficientFundsException()).when(paymentService).processPayment(anyString(), anyDouble());

        assertThatThrownBy(() -> orderService.processPayment(1L, 50.0))
            .isInstanceOf(InsufficientFundsException.class);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.FAILED);
    }
}
