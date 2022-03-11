package coursera.form;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QuestionForm {
    private String description;
    private ArrayList<VariantForm> listOfVariants;
}
