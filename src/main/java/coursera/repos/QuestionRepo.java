package coursera.repos;


import coursera.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    Question findQuestionById(Long id);
}