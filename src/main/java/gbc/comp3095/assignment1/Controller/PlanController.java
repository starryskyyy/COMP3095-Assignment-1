package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Plan;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Service.PlanService;
import gbc.comp3095.assignment1.Service.RecipeService;
import gbc.comp3095.assignment1.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @GetMapping("/addPlan")
    public String addPlanGet(Model model) {
        model.addAttribute("plan", new Plan());
        model.addAttribute("recipes", recipeService.getRecipes());
        return "add_plan";
    }

    @PostMapping("/addPlan")
    public String addPlanPost(Plan plan, @RequestParam("recipeid") int recipeId) {
        // Getting currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);

        // Getting recipe
        Recipe recipe = recipeService.getRecipeById(recipeId);

        plan.setRecipe(recipe);
        userService.updatePlan(user, plan);

        return "add_plan";
    }
}
