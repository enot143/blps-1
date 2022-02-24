package coursera.config;


import coursera.domain.User;
import coursera.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public coursera.config.CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userService.findByEmail(username);
        return coursera.config.CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
}
