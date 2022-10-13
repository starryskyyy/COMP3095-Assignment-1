package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.RecipeIngredient;
import gbc.comp3095.assignment1.Repository.RecipeRepository;
import gbc.comp3095.assignment1.Entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private IngredientService ingredientService;

    public Recipe createRecipe(Recipe recipe) {
        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipe.getName());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setInstruction(recipe.getInstruction());
        newRecipe.setUser(userService.getUserById(recipe.getUser().getId()));
        newRecipe.getIngredients().addAll(recipe.getIngredients().stream().map(ri -> {
            RecipeIngredient newRi = new RecipeIngredient();
            newRi.setIngredient(ingredientService.getIngredientById(ri.getIngredient().getId()));
            newRi.setAmount(ri.getAmount());
            return newRi;
        }).collect(Collectors.toList()));

        return recipeRepository.save(newRecipe);
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    // TODOS:
    // getRecipesByUserId
    // getRecipeById
    // updateRecipe
    public String deleteRecipeById(int id) {
        recipeRepository.deleteById(id);
        return "Recipe has been deleted.";
    }
}
