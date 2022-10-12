package gbc.comp3095.assignment1.Service;

import gbc.comp3095.assignment1.Repository.UserRepository;
import gbc.comp3095.assignment1.UserEntity.User;
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

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {

        Optional<User> optionalUser = userRepository.findById(user.getId());
        User oldUser = null;
        if(optionalUser.isPresent()) {
            oldUser = optionalUser.get();
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
            oldUser.setEmail(user.getEmail());
            oldUser.setAddress(user.getAddress());
            oldUser.setBirthday(user.getBirthday());
        }else{
            return new User();
        }
        return oldUser;
    }

    public String deleteUserById(int id) {
        userRepository.deleteById(id);
        return "User has been deleted.";
    }
}
