package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Repository.RecipeRepository;
import gbc.comp3095.assignment1.Entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private IngredientService ingredientService;

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> getRecipeByUserId(int userId) {
        List<Recipe> newRecipe = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAll();

        for (Recipe recipe: recipes) {
            if (recipe.getUser().getId() == userId) {
                newRecipe.add(recipe);
            }
        }

        return newRecipe;
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findByNameContaining(name);
    }

    public Recipe updateRecipe(Recipe recipe) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipe.getId());
        Recipe newRecipe = null;

        if (optionalRecipe.isPresent()) {
            newRecipe = optionalRecipe.get();
            newRecipe.setName(recipe.getName());
            newRecipe.setUser(recipe.getUser());
            newRecipe.setDescription(recipe.getDescription());
            newRecipe.setInstruction(recipe.getInstruction());
            newRecipe.setIngredients(recipe.getIngredients());

            recipeRepository.save(newRecipe);
        } else {
            return new Recipe();
        }

        return newRecipe;
    }

    public String deleteRecipeById(int id) {
        recipeRepository.deleteById(id);
        return "Recipe has been deleted.";
    }
}
