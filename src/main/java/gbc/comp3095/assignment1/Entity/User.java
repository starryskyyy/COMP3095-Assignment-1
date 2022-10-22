package gbc.comp3095.assignment1.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "password")
        })
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String address;
    private String birthday;

    @ManyToMany(cascade=CascadeType.ALL)
    private Set<Recipe> favoriteRecipes;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User populateInfo(List<String> info) {
        this.username = info.get(0);
        this.firstName = info.get(1);
        this.lastName = info.get(2);
        this.password = info.get(3);
        this.email = info.get(4);
        this.address = info.get(5);

        return this;
    }
}
