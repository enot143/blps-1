package coursera.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@ToString
@EqualsAndHashCode
public class UserCourseKey implements Serializable {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "course_id")
    Long courseId;
}