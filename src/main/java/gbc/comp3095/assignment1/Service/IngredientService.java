package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Ingredient updateIngredient(Ingredient ingredient) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredient.getId());
        Ingredient newIngredient = null;

        if (optionalIngredient.isPresent()) {
            newIngredient = optionalIngredient.get();
            newIngredient.setName(ingredient.getName());

            ingredientRepository.save(newIngredient);
        } else {
            return new Ingredient();
        }

        return newIngredient;
    }

    public String deleteIngredientById(int id) {
        ingredientRepository.deleteById(id);
        return "Ingredient has been deleted.";
    }
}
