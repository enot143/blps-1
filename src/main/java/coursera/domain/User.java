package coursera.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "user_table")
//@ToString(of = {"id", "name_user", "surname", "mid_name", "email", "password_user"})
//@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name_user;
    private String surname;
    private String mid_name;
    private String email;
    private String password_user;
    //    @ManyToOne
//    @JoinColumn(name = "role_id", referencedColumnName = "id")
//    private Role roleEntity;
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),//not sure
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<Role> roles;

}