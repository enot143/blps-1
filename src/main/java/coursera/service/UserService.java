package coursera.service;


import coursera.controller.auth.RegistrationRequest;
import coursera.domain.User;
import coursera.exceptions.EmailExistsException;
import coursera.repos.RoleRepo;
import coursera.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepo userEntityRepository;
    @Autowired
    private RoleRepo roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    public String saveUser(RegistrationRequest registrationRequest) throws EmailExistsException {
        if (userRepo.findUserByEmail(registrationRequest.getEmail()) != null){
            throw new EmailExistsException("There is user with this email already: " + registrationRequest.getEmail());
        }
        User u = new User();
        u.setEmail(registrationRequest.getEmail());
        u.setName_user(registrationRequest.getName());
        u.setSurname(registrationRequest.getSurname());
        u.setMid_name(registrationRequest.getMiddleName());
        u.setRoles(Collections.singletonList(roleEntityRepository.findByName("ROLE_USER")));
        u.setPassword_user(passwordEncoder.encode(registrationRequest.getPassword()));
        userEntityRepository.save(u);
        return "Пользователь успшено зарегистрирован";
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
