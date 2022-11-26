package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Event;
import gbc.comp3095.assignment1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Set<Event> findAllByUserAndDateBetweenOrderByDateAsc(User user, LocalDate today, LocalDate sevenDaysAfter);

}
