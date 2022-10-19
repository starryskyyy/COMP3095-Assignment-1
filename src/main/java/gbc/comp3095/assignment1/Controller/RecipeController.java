package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Service.IngredientService;
import gbc.comp3095.assignment1.Service.RecipeService;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/addRecipe")
    public String addRecipeGet(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("ingredients", ingredientService.getIngredients());
        return "add_recipe";
    }

    @PostMapping("/addRecipe")
    public String addRecipePost(Recipe recipe) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        recipe.setUser(userService.getUserByUsername(username));
        recipeService.createRecipe(recipe);

        return "home";
    }

    @PostMapping("/addRecipes")
    public List<Recipe> addRecipes(@RequestBody List<Recipe> recipes) {
        return recipeService.createRecipes(recipes);
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipeById(@PathVariable int id) {
        Recipe recipe = recipeService.getRecipeById(id);
        if (recipe == null) {
            throw new CustomException("Recipe id not found - " + id);
        }

        return recipe;
    }

    @GetMapping("/recipe/{name}")
    public List<Recipe> searchRecipesByName(@PathVariable String name) {
        return recipeService.getRecipesByName(name);
    }

    @GetMapping("recipes/user/{userId}")
    public List<Recipe> getAllRecipesByUserId(@PathVariable int userId) {
        return recipeService.getRecipeByUserId(userId);
    }

    @PutMapping("/updateRecipe")
    public Recipe updateRecipe(@RequestBody Recipe recipe) {
        return recipeService.updateRecipe(recipe);
    }

    @DeleteMapping("/recipe/{id}")
    public String deleteRecipe(@PathVariable int id) {
        return recipeService.deleteRecipeById(id);
    }
}