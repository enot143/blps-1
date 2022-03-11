package coursera.form;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AddTestForm {
    private Integer minimum;
    private ArrayList<QuestionForm> questions;
}
