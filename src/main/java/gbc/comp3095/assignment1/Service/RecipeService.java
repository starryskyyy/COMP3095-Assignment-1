package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Repository.RecipeRepository;
import gbc.comp3095.assignment1.Entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe createRecipe(Recipe recipe) {
        recipe.setCreatedDate(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(int id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> getRecipeByUserId(Long userId) {
        List<Recipe> newRecipe = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findAll();

        for (Recipe recipe: recipes) {
            User user = recipe.getUser();
            if (user != null && Objects.equals(user.getId(), userId)) {
                newRecipe.add(recipe);
            }
        }

        return newRecipe;
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findByNameContaining(name);
    }

    public String deleteRecipeById(int id) {
        recipeRepository.deleteById(id);
        return "Recipe has been deleted.";
    }
}
