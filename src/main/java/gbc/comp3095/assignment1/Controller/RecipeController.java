package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Service.RecipeService;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Utils.CustomException;
import gbc.comp3095.assignment1.Utils.ImageParser;
import gbc.comp3095.assignment1.Utils.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    private boolean created = false;

    private void addDefaultUsers() {
        JsonParser jsonParser = new JsonParser();
        jsonParser.prepareDefaultUsers();
        userService.createUsers(jsonParser.getDefaultUsers());
    }

    private void addDefaultRecipes() {
        JsonParser jsonParser = new JsonParser();
        jsonParser.prepareDefaultRecipes();
        jsonParser.populateUserToRecipe(userService.getUsers());
        jsonParser.populateImageToRecipes();
        recipeService.createRecipes(jsonParser.getDefaultRecipes());
    }

    @GetMapping("/")
    public String viewHome(Model model, @RequestParam(required = false) String filter) {
        // default recipes
        if (!created) {
            addDefaultUsers();
            addDefaultRecipes();
            created = true;
        }

        if (filter != null) {
            if (Objects.equals(filter, "myRecipes") || Objects.equals(filter, "favoriteRecipes")) {
                // Getting currently logged-in user
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = ((UserDetails) principal).getUsername();

                if (Objects.equals(filter, "myRecipes")) {
                    Long userId = userService.getUserByUsername(username).getId();
                    model.addAttribute("recipes", recipeService.getRecipeByUserId(userId));
                } else {
                    model.addAttribute("recipes", userService.getUserByUsername(username).getFavoriteRecipes());
                }
            } else {
                model.addAttribute("recipes", recipeService.getRecipesByName(filter));
            }
        } else {
            model.addAttribute("recipes", recipeService.getRecipes());
        }

        return "index";
    }

    @PostMapping("/searchRecipeByName")
    public String searchRecipeByName(@RequestParam("recipename") String recipeName) {
        return "redirect:/?filter=" + recipeName;
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
        byte[] encodedFile;
        if (imageFile.isEmpty()) {
            encodedFile = Base64.getEncoder().encode(new ImageParser().getDefaultImage());
        } else {
            encodedFile = Base64.getEncoder().encode(imageFile.getBytes());
        }

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

    @PostMapping(value = "/addRecipe", params = "deletebox")
    public String deleteIngredientBox(Recipe recipe, RedirectAttributes redirectAttributes) {
        int ingredientSize = recipe.getIngredients().size();
        if (ingredientSize > 0) {
            recipe.getIngredients().remove(ingredientSize - 1);
        }

        redirectAttributes.addFlashAttribute("recipe", recipe);
        return "redirect:addRecipe";
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

        // Getting currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);

        for (Recipe r: user.getFavoriteRecipes()) {
            if (r.getId() == id) {
                model.addAttribute("user", user);
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

        recipe.setCreatedDate(LocalDate.now());
        userService.updateFavoriteRecipe(user, recipe);

        redirectAttributes.addFlashAttribute("recipe", recipe);
        return "redirect:recipe/" + recipe.getId() + "?added=true";
    }
}