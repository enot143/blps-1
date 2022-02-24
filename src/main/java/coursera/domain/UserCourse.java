package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.sql.Date;
import javax.persistence.*;

@Entity
@Data
@Table(name = "user_course")
@ToString(of = {"user", "course", "start_date", "end_date", "progress", "certificate_status"})
@EqualsAndHashCode
public class UserCourse {

    @EmbeddedId
    UserCourseKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    Course course;
    private Date start_date;
    private Date end_date;
    private Integer progress;
    private boolean certificate_status;
}