package coursera.repos;

import coursera.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface TestRepo extends JpaRepository<Test, Long> {
    Test findTestById(Long id);
    @Query("SELECT q.description FROM Question q WHERE q.test.id = :test_id")
    ArrayList<String> findQuestions(@Param("test_id") Long test_id);

    @Query("SELECT COUNT (*) FROM Question q join Test t ON q.test.id = t.id WHERE t.id = :test_id")
    Long findNumberOfQuestionsInTest(@Param("test_id") Long test_id);


}