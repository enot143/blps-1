package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table
@ToString(of = {"id", "question", "number_of_variant", "description", "correct"})
@EqualsAndHashCode(of = {"id"})
public class Variant {
    @Id
    @Column(name = "variant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    private Question question;
    private Integer number_of_variant;
    private String description;
    private boolean correct;
}