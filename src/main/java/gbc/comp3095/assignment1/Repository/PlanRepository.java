package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Plan;
import gbc.comp3095.assignment1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    Set<Plan> findAllByUserAndDateBetweenOrderByDateAsc(User user, LocalDate today, LocalDate sevenDaysAfter);
}
