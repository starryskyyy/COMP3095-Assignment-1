package gbc.comp3095.assignment1.Security;

import gbc.comp3095.assignment1.Entity.User;
import gbc.comp3095.assignment1.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Could not find User");
        }
        return new MyUserDetails(user);
    }
}
