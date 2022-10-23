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

        // ingredients
        List<List<Ingredient>> ingredients = new ArrayList<>();
        ingredients.add(Arrays.asList(
                new Ingredient("Sweet Paprika", "2 teaspoons"),
                new Ingredient("Chili Powder", "2 teaspoons"),
                new Ingredient("Onion Powder", "1/2 teaspoons"),
                new Ingredient("Garlic Powder", "1/2 teaspoons"),
                new Ingredient("Ground Cumin", "1/2 teaspoons"),
                new Ingredient("Kosher Salt", "as you wish"),
                new Ingredient("Ground Black Pepper", "as you wish"),
                new Ingredient("Short Ribs", "4 pounds"),
                new Ingredient("Canola Oil", "3 tablespoons"),
                new Ingredient("Yellow Onion", "2 cups"),
                new Ingredient("Garlic", "4"),
                new Ingredient("Mashed Potato", "as you wish"),
                new Ingredient("Coca-Cola", "2.5 cups")
        ));


        List<byte[]> images = new ImageParser().getImageBytes();
        for (int i = 0; i < images.size(); ++i) {
            Recipe recipe = new Recipe();
            byte[] encodedFile = Base64.getEncoder().encode(images.get(i));
            String encodedFileString = new String(encodedFile, StandardCharsets.UTF_8);
            recipe.setImageFile(encodedFileString);

            if (i == 0) {
                recipe.setName("Instant Pot Cola-Braised Short Ribs");
                recipe.setDescription("With the help of an Instant Pot® and some cola, these saucy short ribs will give you that \"braised all day\" flavor and tenderness in just under two hours. The acidity of the cola not only helps tenderize the beef, but also rounds out the spices and balances the dish.");
                recipe.setInstruction(
                                "1. Combine the paprika, chili powder, onion powder, garlic powder, cumin, 1 tablespoon salt and 2 teaspoons pepper in a large bowl. Add the short ribs to the bowl, rub the spice mixture all over and allow to rest for 10 minutes.\n" +
                                "2. Set a 6-quart Instant Pot® to high saute (see Cook's Note) and add 2 tablespoons of the oil once it is hot. Working in batches, sear all 4 sides of each short rib, about 2 minutes per side. Set the short ribs aside on a plate.\n" +
                                "3. With the pot still on high saute, add the remaining 1 tablespoon oil. Add the onions and cook, stirring occasionally with a wooden spoon to scrape up bits on the bottom, until softened, about 3 minutes. Add the tomato paste and garlic and cook until the tomato paste is deeply toasted, 1 to 2 minutes. Add the cola, Worcestershire sauce, bay leaves and 1/2 cup of the beef broth and stir to combine. Put the ribs back into the pot along with any juices on the plate and turn to coat with the liquid. Follow the manufacturer's guide for locking the lid and preparing to cook. Set to pressure cook on high for 45 minutes.\n" +
                                "4. After the pressure-cook cycle is complete, follow the manufacturer's guide for quick release and wait until the quick-release cycle is complete. Careful of any remaining steam, unlock and remove the lid and transfer the short ribs to a large plate.\n" +
                                "5. Use a ladle to skim any excess fat off the top of the liquid and then turn the pot back to high saute. While the liquid begins to simmer, combine the remaining 2 tablespoons of beef broth with the cornstarch in a small bowl to make a slurry. Whisk in the slurry and cook, whisking intermittently, until the sauce is reduced and thickened to a syrup-like consistency, 15 to 20 minutes. Season the sauce with salt and transfer the short ribs back to the pot to coat with the sauce. Serve the short ribs hot with the sauce over mashed potatoes, polenta or rice, if using. ");
                for (List<Ingredient> ings : ingredients) {
                    for (Ingredient ingredient: ings) {
                        recipe.getIngredients().add(ingredient);
                    }
                }
            } else {
                recipe.setName("Recipe" + (i + 1));
                recipe.setDescription("Description" + (i + 1));
                recipe.setInstruction("Instruction" + (i + 1));
            }

            recipe.setUser(users.get(random.nextInt(users.size())));
            recipeService.createRecipe(recipe);
        }
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(required = false) String filter) {
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