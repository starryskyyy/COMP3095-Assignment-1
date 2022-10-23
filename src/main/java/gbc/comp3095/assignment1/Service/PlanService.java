package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Plan;
import gbc.comp3095.assignment1.Repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }
}