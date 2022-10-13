package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.Entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
