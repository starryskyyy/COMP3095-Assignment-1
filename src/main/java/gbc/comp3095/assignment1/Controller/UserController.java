package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Repository.UserRepository;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(path = "/home")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home.html");
        return modelAndView;
    }

    @GetMapping(path = "/signup")
    public ModelAndView signupGet() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup.html");
        return modelAndView;
    }

    @PostMapping(path = "/signup")
    public void signupPost(
            @RequestParam("username") String username,
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam("birthday") String birthday
    ) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        user.setBirthday(birthday);
        user.setEnabled(true);
        user.getRoles().add(new Role(1));

        userService.createUser(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
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
