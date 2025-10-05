package Lab10.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @GetMapping
    public ResponseEntity<String> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("ok, " + (user != null ? user.getUsername() : "anon"));
    }
}
