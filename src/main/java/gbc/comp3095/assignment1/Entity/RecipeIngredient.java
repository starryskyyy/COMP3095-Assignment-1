package gbc.comp3095.assignment1.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredient {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Ingredient ingredient;

    private String amount;
}