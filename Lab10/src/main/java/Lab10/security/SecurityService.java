package Lab10.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public boolean hasPermission(UserDetails user, String permission) {
        if (permission == null) return false;
        boolean isAdmin = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.contains("ADMIN"));
        if (isAdmin) return true;

        return "VIEW_ORDERS".equals(permission);
    }
}
