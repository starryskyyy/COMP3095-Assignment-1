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
@Table(name = "shoppings")
public class Shopping {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade=CascadeType.ALL)
    Recipe recipe;
    @ManyToOne(cascade=CascadeType.ALL)
    User user;
    @ManyToMany
    List<Ingredient> ingredients = new ArrayList<>();
}