package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Shopping;
import gbc.comp3095.assignment1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Integer> {
    public List<Shopping> findAllByUser(User user);
}
