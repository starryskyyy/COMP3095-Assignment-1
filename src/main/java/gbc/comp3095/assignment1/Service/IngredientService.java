package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        return ingredientRepository.saveAll(ingredients);
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(int id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    // TODOS:
    // updateIngredient
    // deleteIngredient
}
