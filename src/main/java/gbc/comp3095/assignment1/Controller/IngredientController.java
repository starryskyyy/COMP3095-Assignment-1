package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Service.IngredientService;
import gbc.comp3095.assignment1.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @PostMapping("/addIngredient")
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.createIngredient(ingredient);
    }

    @PostMapping("/addIngredients")
    public List<Ingredient> addIngredients(@RequestBody List<Ingredient> ingredients) {
        return ingredientService.createIngredients(ingredients);
    }

    @GetMapping("/ingredients")
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getIngredients();
    }
}