package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table
@ToString(of = {"id", "week", "minimum"})
@EqualsAndHashCode(of = {"id"})
public class Test {
    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "week_id", referencedColumnName = "week_id")
    private Week week;
    private Integer minimum;
}