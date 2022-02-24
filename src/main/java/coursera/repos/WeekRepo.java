package coursera.repos;

import coursera.domain.Week;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekRepo extends JpaRepository<Week, Long> {
    Week findWeekById(Long id);
}
