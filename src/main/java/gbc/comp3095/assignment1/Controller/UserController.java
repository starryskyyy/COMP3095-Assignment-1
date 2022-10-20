package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Repository.UserRepository;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    private boolean created = false;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/home")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home.html");
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        User user = new User();
        if (created == false) {
            // for testing purpose
            user.setUsername("user");
            user.setPassword("ps");
            user.setAddress("");
            user.setBirthday("");
            user.setFirstName("");
            user.setLastName("");

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.getRoles().add(new Role(1));
            userService.createUser(user);

            created = true;
        }

        return "login";
    }

    @GetMapping(path = "/signup")
    public String signupGet(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping(path = "/signup")
    public String signupPost(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.getRoles().add(new Role(1));
        userService.createUser(user);
        return "login";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/addUsers")
    public List<User> addUsers(@RequestBody List<User> users) {
        return userService.createUsers(users);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new CustomException("User id not found - " + id);
        }

        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getUsers();
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
}
