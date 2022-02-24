package coursera.dto;

import java.util.ArrayList;

public class QuestionDTO {
    private Long id;
    private String description;
    private ArrayList<VariantDTO> listOfAnswers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<VariantDTO> getListOfAnswers() {
        return listOfAnswers;
    }

    public void setListOfAnswers(ArrayList<VariantDTO> listOfAnswers) {
        this.listOfAnswers = listOfAnswers;
    }
}