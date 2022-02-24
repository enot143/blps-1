package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table
@ToString(of = {"id", "test", "description"})
@EqualsAndHashCode(of = {"id"})
public class Question {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "test_id")
    private Test test;
    private String description;
}