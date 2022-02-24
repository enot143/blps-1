package coursera.repos;

import coursera.domain.User;
import coursera.domain.UserTest;
import coursera.domain.UserTestKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTestRepo extends JpaRepository<UserTest, UserTestKey> {

    UserTest getUserTestByUser(User user);

    @Query("SELECT ut.attempts FROM UserTest ut WHERE ut.test.id = :test_id AND ut.user.id = :user_id")
    Long getAttempts(@Param("test_id") Long test_id, @Param("user_id") Long user_id);

    @Query("SELECT ut.progress FROM UserTest ut WHERE ut.test.id = :test_id AND ut.user.id = :user_id")
    Long getProgress(@Param("test_id") Long test_id, @Param("user_id") Long user_id);

    @Modifying
    @Query("UPDATE UserTest ut SET ut.attempts = ut.attempts - 1 WHERE ut.test.id = :test_id AND ut.user.id = :user_id")
    void setAttempts(@Param("test_id") Long test_id, @Param("user_id") Long user_id);

    @Modifying
    @Query("UPDATE UserTest ut SET ut.progress = :progress WHERE ut.test.id = :test_id AND ut.user.id = :user_id")
    void setProgress(@Param("test_id") Long test_id, @Param("user_id") Long user_id, @Param("progress") Integer progress);


    @Modifying
    @Query("UPDATE UserTest ut SET ut.status = true WHERE ut.test.id = :test_id AND ut.user.id = :user_id")
    void setStatus(@Param("test_id") Long test_id, @Param("user_id") Long user_id);



}