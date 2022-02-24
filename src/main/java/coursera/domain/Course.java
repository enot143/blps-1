package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table
@ToString(of = {"id", "cost", "week_quantity", "university"})
@EqualsAndHashCode(of = {"id"})
public class Course {
    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cost;
    private Integer week_quantity;
    private String university;
}