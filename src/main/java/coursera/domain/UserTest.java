package coursera.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.sql.Date;
import javax.persistence.*;

@Entity
@Data
@Table(name = "user_test")
@ToString(of = {"user", "test", "progress", "status", "attempts"})
@EqualsAndHashCode
public class UserTest {

    @EmbeddedId
    UserTestKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "test_id", referencedColumnName = "test_id")
    Test test;
    private Integer progress;
    private boolean status;
    private Integer attempts;
}