package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

    @PutMapping("/updateuser")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUserById(id);
    }
}
