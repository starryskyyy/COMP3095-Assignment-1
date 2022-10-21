package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.User;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }

    @GetMapping("/addRecipe")
    public String addRecipeGet(Model model, @ModelAttribute("recipe") Recipe previousRecipe) {
        model.addAttribute("recipe", previousRecipe);
        return "add_recipe";
    }

    @PostMapping(value = "/addRecipe", params = "submit")
    public String addRecipePost(Recipe recipe, @RequestParam("image") MultipartFile imageFile) throws IOException {
        // Inserting current logged-in user into Recipe
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

    @PostMapping(value = "/addRecipe", params = "addbox")
    public String addIngredientBox(Recipe recipe, RedirectAttributes redirectAttributes) {
        recipe.getIngredients().add(new Ingredient());

        redirectAttributes.addFlashAttribute("recipe", recipe);
        return "redirect:addRecipe";
    }

    @GetMapping("/recipes")
    public String getAllRecipes(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());
        return "recipes";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(Model model, @ModelAttribute("recipe") Recipe previousRecipe, @PathVariable int id) {
        Recipe recipe = previousRecipe;
        if (recipe.getName() == null) {
            recipe = recipeService.getRecipeById(id);
            if (recipe == null) {
                throw new CustomException("Recipe id not found - " + id);
            }
        }
        model.addAttribute("recipe", recipe);
        return "view_recipe";
    }

    @PostMapping("/recipe")
    public String addFavoriteRecipe(Recipe recipe, RedirectAttributes redirectAttributes) {
        // Getting currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);
        user.getFavoriteRecipes().add(recipe);

        userService.updateUser(user);

        System.out.println(recipe);
        redirectAttributes.addFlashAttribute("recipe", recipe);
        return "redirect:recipe/" + recipe.getId() + "?added=true";
    }
}