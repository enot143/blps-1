package coursera.controller.auth;

import coursera.config.jwt.JwtProvider;
import coursera.domain.User;
import coursera.exceptions.EmailExistsException;
import coursera.repos.UserRepo;
import coursera.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws EmailExistsException {
       return userService.saveUser(registrationRequest);
    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User userEntity = userService.findByLoginAndPassword(request.getUsername(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getEmail());
        return new AuthResponse(token, userEntity.getRoles().toString());
//        return new AuthResponse(token);
    }
//    @GetMapping("/info")
//    publ
//        return new AuthResponse(token,  userEntity.getRoleEntity().getName());ic AuthResponse info() {
//        User userEntity = userService.findByLoginAndPassword(request.getUsername(), request.getPassword());
//        String token = jwtProvider.generateToken(userEntity.getEmail());
//    }

}
