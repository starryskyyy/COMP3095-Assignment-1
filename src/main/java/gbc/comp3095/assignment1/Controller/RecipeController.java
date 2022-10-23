package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Service.RecipeService;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Utils.CustomException;
import gbc.comp3095.assignment1.Utils.ImageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class RecipeController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    private boolean created = false;

    private void addDefaultUsers() {
        List<List<String>> information = new ArrayList<>();
        information.add(Arrays.asList("user", "user", "user", "ps", "user@gmail.com", "130 User st."));
        information.add(Arrays.asList("hoonie", "Seunghun", "Yim", "ps", "yimsh@gmail.com", "5700 Yonge st."));
        information.add(Arrays.asList("danny", "Danny", "Nguyen", "ps", "danny@gmail.com", "124 Dundas st."));
        information.add(Arrays.asList("yoonie", "Yoonhee", "Kim", "ps", "yoonie@gmail.com", "232 Jarvis st."));
        information.add(Arrays.asList("lisa", "Elizaveta", "Vygovskaia", "ps", "liz@gmail.com", "3433 Richmond st."));

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            User user = new User().populateInfo(information.get(i));
            user.setId(i + 1L);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.getRoles().add(new Role(1));

            users.add(user);
        }

        userService.createUsers(users);
    }

    private void addDefaultRecipes() {
        Random random = new Random();
        List<User> users = userService.getUsers();

        List<byte[]> images = new ImageParser().getImageBytes();
        for (int i = 0; i < images.size(); ++i) {
            Recipe recipe = new Recipe();
            byte[] encodedFile = Base64.getEncoder().encode(images.get(i));
            String encodedFileString = new String(encodedFile, StandardCharsets.UTF_8);
            recipe.setImageFile(encodedFileString);

            recipe.setName("Recipe" + (i + 1));
            recipe.setDescription("Description" + (i + 1));
            recipe.setInstruction("Instruction" + (i + 1));
            recipe.setUser(users.get(random.nextInt(users.size())));

            recipeService.createRecipe(recipe);
        }
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        // default recipes
        if (!created) {
            addDefaultUsers();
            addDefaultRecipes();
            created = true;
        }

        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }

    @GetMapping("/myRecipes")
    public String viewMyRecipes(Model model) {
        // Getting Currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Long userId = userService.getUserByUsername(username).getId();

        model.addAttribute("recipes", recipeService.getRecipeByUserId(userId));
        return "index";
    }

    @GetMapping("/favoriteRecipes")
    public String viewFavoriteRecipes(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        model.addAttribute("recipes", userService.getUserByUsername(username).getFavoriteRecipes());
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

        userService.updateFavoriteRecipe(user, recipe);

        redirectAttributes.addFlashAttribute("recipe", recipe);
        return "redirect:recipe/" + recipe.getId() + "?added=true";
    }
}