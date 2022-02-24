package coursera.form;

import java.util.ArrayList;

public class AnswerForm {
    private Long question_id;
    private ArrayList<Long> variant_id;

    public Long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public ArrayList<Long> getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(ArrayList<Long> variant_id) {
        this.variant_id = variant_id;
    }
}