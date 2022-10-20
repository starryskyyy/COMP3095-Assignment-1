package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private IngredientService ingredientService;

    private int ingredientLength = 1;

    @GetMapping("/addRecipe")
    public String addRecipeGet(Model model) {
        Recipe recipe = new Recipe();

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientLength; ++i) {
            ingredients.add(new Ingredient());
        }

        recipe.setIngredients(ingredients);
        model.addAttribute("recipe", recipe);
        return "add_recipe";
    }

    @PostMapping("/addRecipe")
    public String addRecipePost(Recipe recipe, @RequestParam("image") MultipartFile imageFile) throws IOException {
        ingredientLength = 1;

        // Inserting current logged in user into Recipe
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        recipe.setUser(userService.getUserByUsername(username));
        // Encoding Image
        byte[] encodedFile = Base64.getEncoder().encode(imageFile.getBytes());
        String encodedFileString = new String(encodedFile, StandardCharsets.UTF_8);
        recipe.setImageFile(encodedFileString);

        recipeService.createRecipe(recipe);

        return "redirect:";
    }

    @GetMapping("/addIngredientBox")
    public String addIngredientBox() {
        ingredientLength++;
        return "redirect:addRecipe";
    }

    @GetMapping("/recipes")
    public String getAllRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes";
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