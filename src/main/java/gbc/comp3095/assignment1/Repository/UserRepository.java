package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User getUserByUsername(@Param("username") String username);
}
