package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

}
