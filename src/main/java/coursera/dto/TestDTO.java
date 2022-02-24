package coursera.dto;

import coursera.domain.Test;
import java.util.ArrayList;

public class TestDTO {
    private Test test;
    private ArrayList<QuestionDTO> listOfQuestions;

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public ArrayList<QuestionDTO> getListOfQuestions() {
        return listOfQuestions;
    }

    public void setListOfQuestions(ArrayList<QuestionDTO> listOfQuestions) {
        this.listOfQuestions = listOfQuestions;
    }
}