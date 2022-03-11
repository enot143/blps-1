package coursera.form;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AnswerForm {
    private Long question_id;
    private ArrayList<Long> variant_id;
}