package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table
@ToString(of = {"id", "number_of_weeks", "course", "number_of_lectures", "deadline"})
@EqualsAndHashCode(of = {"id"})
public class Week {
    @Id
    @Column(name = "week_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number_of_weeks;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;
    private Integer number_of_lectures;
    private Date deadline;
}