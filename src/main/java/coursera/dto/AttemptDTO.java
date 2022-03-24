package coursera.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttemptDTO implements Serializable {
    Long testId;
    Integer quantity;
    Long userId;
}
