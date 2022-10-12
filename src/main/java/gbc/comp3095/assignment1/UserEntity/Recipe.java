package gbc.comp3095.assignment1.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
