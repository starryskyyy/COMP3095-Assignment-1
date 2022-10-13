package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}