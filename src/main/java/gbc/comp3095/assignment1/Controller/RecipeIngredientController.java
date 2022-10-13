package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Service.RecipeIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeIngredientController {
    @Autowired
    private RecipeIngredientService recipeIngredientService;
}
