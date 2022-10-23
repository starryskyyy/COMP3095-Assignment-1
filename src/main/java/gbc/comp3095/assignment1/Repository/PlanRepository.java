package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
}
