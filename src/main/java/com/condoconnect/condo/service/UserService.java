package com.condoconnect.condo.service;

import com.condoconnect.condo.dto.UserDto;
import com.condoconnect.condo.repository.IUsers;
import com.condoconnect.condo.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final IUsers repository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserService(IUsers repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> getAllUsers() {
        logger.info("User " + getLogger() + "listing user");
        return repository.findAll();
    }
    public User createUser(User user) {
        String encoder = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encoder);
        logger.info("User " + getLogger() + "creating user");
        return repository.save(user);
    }
    public User updatedUser(User user) {
        String encoder = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encoder);
        logger.info("User " + getLogger() + "updating user" + user);
        return repository.save(user);
    }
    public Boolean deleteUser(Integer id) {
        repository.deleteById(id);
        logger.info("User " + getLogger() + "deleting user");
        return true;
    }

    public Token generatedToken(UserDto user) {
        User userFind = repository.findByNameOrEmail(user.getUsername(), user.getEmail());

        if (userFind != null) {
            boolean valid = passwordEncoder.matches(user.getPassword(), userFind.getPassword());
            if (valid) {
                return new Token(TokenUtil.createToken(userFind));
            }
        }

        return null;
    }

    private String getLogger(){
        Authentication userLogger = SecurityContextHolder.getContext().getAuthentication();
        if(userLogger instanceof AnonymousAuthenticationToken){
            return userLogger.getName();
        }
        return "Null";
    }



}
