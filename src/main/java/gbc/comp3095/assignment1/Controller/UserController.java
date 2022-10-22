package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
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

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new CustomException("User id not found - " + id);
        }

        return user;
    }
}
