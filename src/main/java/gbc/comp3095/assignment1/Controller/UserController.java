/*********************************************************************************
* Project: RecipeShare
* Assignment: Assignment #1
* Author(s): Seunghun Yim, Danny Nguyen, Yoonhee Kim, Elizaveta Vygovskaia
* Student Number: 101325908, 100882851, 101277278, 101337015
* Date: October 23rd, 2022
* Description: It is a controller class for user entity.
* Logging in, Singing up, Viewing profile are implemented
*********************************************************************************/

package gbc.comp3095.assignment1.Controller;

import gbc.comp3095.assignment1.Entity.Role;
import gbc.comp3095.assignment1.Repository.UserRepository;
import gbc.comp3095.assignment1.Service.UserService;
import gbc.comp3095.assignment1.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @GetMapping("/viewProfile")
    public String viewProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);

        model.addAttribute("user", user);
        return "view_profile";
    }

    @GetMapping(path = "/loadForgotPassword")
    public String loadForgotPassword() {
        return "forget_password";
    }

    @GetMapping(path ="/loadResetPassword/{username}")
    public String loadResetPassword(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        return "reset_password";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email, @RequestParam String username, HttpSession session){
        User user = userService.findUserByEmailAndUsername(email, username);

        if(user != null){
            return "redirect:/loadResetPassword/" + user.getUsername();
        } else {
            session.setAttribute("msg", "invalid credentials");
            return "forget_password";
        }
    }

    @PostMapping(path = "/changePassword")
    public String resetPassword(@RequestParam String password, @RequestParam String username, HttpSession session) {

        User user = userService.getUserByUsername(username);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        User updateUser = userService.updateUser(user);

        if(updateUser != null)
        {
            session.setAttribute("msg", "Password Changed Successfully");
        }

        return "redirect:/loadForgotPassword";
    }

    @GetMapping("/editProfile/{username}")
    public String updateProfileGet(Model model, @PathVariable String username){
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "/update_profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User user) {
        userService.updateUser(user);
        return "/view_profile";
    }
}
