package Lab10.test;

import Lab10.security.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AuthorizationTest {

    @Autowired SecurityService securityService;

    @Test
    void checkPermissions_AdminRole_HasAccess() {
        User admin = new User("admin", "password", java.util.List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        boolean has = securityService.hasPermission(admin, "DELETE_ORDERS");
        assertThat(has).isTrue();
    }

    @Test
    void checkPermissions_UserRole_NoAccess() {
        User user = new User("user", "password", java.util.List.of(new SimpleGrantedAuthority("ROLE_USER")));
        boolean has = securityService.hasPermission(user, "DELETE_ORDERS");
        assertThat(has).isFalse();
    }
}
