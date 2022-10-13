package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.RecipeIngredient;
import gbc.comp3095.assignment1.Repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeIngredientService {
    @Autowired
    private RecipeIngredientRepository riRepository;

    public RecipeIngredient createRecipeIngredient(RecipeIngredient ri) {
        return riRepository.save(ri);
    }
}
