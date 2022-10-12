package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
