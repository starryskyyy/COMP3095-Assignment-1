package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Repository.UserRepository;
import gbc.comp3095.assignment1.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> createUsers(List<User> users){
        return userRepository.saveAll(users);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        User newUser = null;

        if (optionalUser.isPresent()) {
            newUser = optionalUser.get();
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            newUser.setAddress(user.getAddress());
            newUser.setBirthday(user.getBirthday());

            userRepository.save(newUser);
        } else {
            return new User();
        }

        return newUser;
    }

    public String deleteUserById(Long id) {
        userRepository.deleteById(id);
        return "User has been deleted.";
    }
}
