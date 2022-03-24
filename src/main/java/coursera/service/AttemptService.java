package coursera.service;

import coursera.config.CustomUserDetails;
import coursera.domain.Test;
import coursera.domain.User;
import coursera.dto.AttemptDTO;
import coursera.exceptions.Response;
import coursera.form.AddAttemptsForm;
import coursera.repos.TestRepo;
import coursera.repos.UserRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Service
public class AttemptService {
    private final RabbitTemplate rabbitTemplate;
    private final String topicExchangeName = "spring-boot";
    User user;
    @Autowired
    UserRepo userRepo;
    @Autowired
    TestRepo testRepo;
    public AttemptService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<?> add(Long t, AddAttemptsForm form) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepo.findUserByEmail(userDetails.getUsername());
        AttemptDTO attemptDTO = new AttemptDTO();
        attemptDTO.setTestId(t);
        attemptDTO.setQuantity(form.getQuantity());
        attemptDTO.setUserId(user.getId());
        rabbitTemplate.convertAndSend(topicExchangeName, "attempts.key", convertObjectToBytes(attemptDTO));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public static byte[] convertObjectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ObjectOutputStream ous = new ObjectOutputStream(boas)) {
            ous.writeObject(obj);
            return boas.toByteArray();
        }
    }
}
