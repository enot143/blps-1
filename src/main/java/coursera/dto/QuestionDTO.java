package coursera.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QuestionDTO {
    private Long id;
    private String description;
    private ArrayList<VariantDTO> listOfAnswers;
}