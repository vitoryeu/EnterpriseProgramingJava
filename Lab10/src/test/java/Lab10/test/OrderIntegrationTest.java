package Lab10.test;

import Lab10.domain.Order;
import Lab10.dto.OrderDTO;
import Lab10.repository.OrderRepository;
import Lab10.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real Postgres
class OrderIntegrationTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @Transactional
    @Rollback
    void orderFlow_CompleteScenario_UsesRealPostgres() {
        OrderDTO dto = new OrderDTO("1", "Test Product", 150.0);
        Order created = orderService.createOrder(dto);
        Order found = orderRepository.findById(created.getId()).orElseThrow();
        assertThat(found.getProductName()).isEqualTo("Test Product");
        assertThat(found.getId()).isNotNull();
    }
}
