package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Service.IngredientService;
import gbc.comp3095.assignment1.Service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/ingredient/{id}")
    public Ingredient getIngredientById(@PathVariable int id) {
        return ingredientService.getIngredientById(id);
    }

    @PutMapping("/updateIngredient")
    public Ingredient updateIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.updateIngredient(ingredient);
    }

    @DeleteMapping("/ingredient/{id}")
    public String deleteIngredient(@RequestBody int id) {
        return ingredientService.deleteIngredientById(id);
    }
}