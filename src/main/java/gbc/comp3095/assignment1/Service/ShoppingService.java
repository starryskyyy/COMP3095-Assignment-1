package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Entity.Ingredient;
import gbc.comp3095.assignment1.Entity.Shopping;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Repository.ShoppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingService {
    @Autowired
    private ShoppingRepository shoppingRepository;

    public Shopping getShoppingById(int id) {
        return shoppingRepository.findById(id).orElse(null);
    }

    public List<Shopping> getShoppingsByUser(User user) {
        return shoppingRepository.findAllByUser(user);
    }

    public Shopping createShopping(Shopping shopping) {
        return shoppingRepository.save(shopping);
    }

    public void deleteIngredientByIds(int shoppingId, int ingredientId) {
        Shopping shopping = shoppingRepository.findById(shoppingId).orElse(null);
        List<Ingredient> ingredients = shopping.getIngredients();

        for (int i = 0; i < ingredients.size(); ++i) {
            if (ingredients.get(i).getId() == ingredientId) {
                ingredients.remove(i);
                break;
            }
        }

        shopping.setIngredients(ingredients);

        shoppingRepository.save(shopping);
    }

    public Shopping updateShopping(Shopping shopping) {
        return shoppingRepository.save(shopping);
    }
}
