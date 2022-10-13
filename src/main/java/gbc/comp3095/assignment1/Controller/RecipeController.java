package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping("/addRecipe")
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipeById(@PathVariable int id) {
        return recipeService.getRecipeById(id);
    }

    @DeleteMapping("/recipe/{id}")
    public String deleteRecipe(@PathVariable int id) {
        return recipeService.deleteRecipeById(id);
    }
}