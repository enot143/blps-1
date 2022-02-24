package coursera.service;


import coursera.domain.Role;
import coursera.domain.User;
import coursera.repos.RoleRepo;
import coursera.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userEntityRepository;
    @Autowired
    private RoleRepo roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User userEntity) {
        Role userRole = roleEntityRepository.findByName("ROLE_USER");
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword_user(passwordEncoder.encode(userEntity.getPassword_user()));
        return userEntityRepository.save(userEntity);
    }

    public User findByEmail(String email) {
        return userEntityRepository.findUserByEmail(email);
    }

    public User findByLoginAndPassword(String login, String password) {
        User userEntity = findByEmail(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword_user())) {
                return userEntity;
            }
        }
        return null;
    }
}
