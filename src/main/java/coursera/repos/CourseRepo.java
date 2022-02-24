package coursera.repos;

import coursera.domain.Course;
import coursera.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface CourseRepo extends JpaRepository<Course, Long> {

    @Query("SELECT t.id FROM Course c join Week w ON w.course.id = :course_id JOIN Test t ON t.week.id = w.id WHERE c.id = :course_id")
    ArrayList<Long> findAllTestsOfCourse(@Param("course_id") Long course_id);
}