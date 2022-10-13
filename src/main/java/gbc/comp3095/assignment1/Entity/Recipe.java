package gbc.comp3095.assignment1.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private String instruction;

    @ManyToOne
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();
}
