package Lab10.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityTest {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    @Test
    void accessProtectedEndpoint_NoAuth_Returns401() {
        ResponseEntity<String> resp = rest.getForEntity("http://localhost:" + port + "/api/admin/orders", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void accessProtectedEndpoint_ValidAuth_Returns200() {
        TestRestTemplate withAuth = rest.withBasicAuth("admin", "password");
        ResponseEntity<String> resp = withAuth.getForEntity("http://localhost:" + port + "/api/admin/orders", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
