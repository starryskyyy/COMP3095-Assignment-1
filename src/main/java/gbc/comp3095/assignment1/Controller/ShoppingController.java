package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Recipe;
import gbc.comp3095.assignment1.Entity.Shopping;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Service.RecipeService;
import gbc.comp3095.assignment1.Service.ShoppingService;
import gbc.comp3095.assignment1.Service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShoppingController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private ShoppingService shoppingService;

    @GetMapping("/viewMyShoppingLists")
    public String viewShoppingLists(Model model) {
        // Getting currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);

        model.addAttribute("shoppings", shoppingService.getShoppingsByUser(user));

        return "view_shoppings";
    }

    @GetMapping("/viewMyShoppingLists/{id}")
    public String viewShoppingList(Model model, @PathVariable int id) {
        model.addAttribute("shopping", shoppingService.getShoppingById(id));

        return "view_shopping";
    }

    @GetMapping("/viewMyShoppingLists/{shoppingId}/deleteIngredient/{ingredientId}")
    public String deleteIngredientOfShoppingList(
            @PathVariable int shoppingId,
            @PathVariable int ingredientId)
    {
        shoppingService.deleteIngredientByIds(shoppingId, ingredientId);

        return "redirect:/viewMyShoppingLists/" + shoppingId;
    }

    @GetMapping("/recipe/{recipeId}/addShopping")
    public String addShopping(
            Model model,
            @PathVariable int recipeId,
            @ModelAttribute("recipe") Recipe previousRecipe) {
        if (previousRecipe.getId() != 0) {
            model.addAttribute("ingredients", previousRecipe.getIngredients());
        } else {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            model.addAttribute("ingredients", recipe.getIngredients());
        }

        return "add_shopping";
    }

    @PostMapping("/recipe/{recipeId}/addShopping")
    public String addIngredientToShoppingList(
            @PathVariable int recipeId,
            @RequestParam("ingredients") List<String> selectedIngredients)
    {
        // Getting currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        List<Ingredient> ingredients = new ArrayList<>();
        for (String id : selectedIngredients) {
            ingredients.add(recipeService.getIngredientById(recipeId, Integer.parseInt(id)));
        }

        Shopping shopping = new Shopping();
        shopping.setUser(userService.getUserByUsername(username));
        shopping.setRecipe(recipeService.getRecipeById(recipeId));
        shopping.setIngredients(ingredients);
        shoppingService.createShopping(shopping);

        return "redirect:";
    }
}
