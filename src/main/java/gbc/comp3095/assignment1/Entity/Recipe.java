package gbc.comp3095.assignment1.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    private String imageFile;

    @ManyToOne
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();
}