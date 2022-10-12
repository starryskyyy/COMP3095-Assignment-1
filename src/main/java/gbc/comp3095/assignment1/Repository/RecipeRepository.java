package gbc.comp3095.assignment1.Repository;

import gbc.comp3095.assignment1.UserEntity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
}
