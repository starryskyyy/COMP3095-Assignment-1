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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    private boolean created = false;

    @GetMapping("/login")
    public String login() {
        if (!created) {
            // Initial Values
            List<List<String>> information = new ArrayList<>();
            information.add(Arrays.asList("user", "user", "user", "ps", "user@gmail.com", "130 User st."));
            information.add(Arrays.asList("hoonie", "Seunghun", "Yim", "ps", "yimsh@gmail.com", "5700 Yonge st."));
            information.add(Arrays.asList("danny", "Danny", "Nguyen", "ps", "danny@gmail.com", "124 Dundas st."));
            information.add(Arrays.asList("yoonie", "Yoonhee", "Kim", "ps", "yoonie@gmail.com", "232 Jarvis st."));
            information.add(Arrays.asList("lisa", "Elizaveta", "Vygovskaia", "ps", "liz@gmail.com", "3433 Richmond st."));

            List<User> users = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                User user = new User().populateInfo(information.get(i));
                user.setId(i + 1L);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                user.getRoles().add(new Role(1));

                users.add(user);
            }

            userService.createUsers(users);

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
