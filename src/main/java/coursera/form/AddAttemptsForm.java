package coursera.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AddAttemptsForm {
    @NotNull(message = "quantity must be not null")
    @Min(value = 1, message = "quantity should not be less than 1")
    Integer quantity;
}
