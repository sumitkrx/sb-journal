package com.sumitkrx.journal.service;

import com.sumitkrx.journal.entity.User;
import com.sumitkrx.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User user) {
        String currentUserName = user.getUserName();
        String currentPwd = user.getPassword();

        boolean isValid = currentUserName != null && !currentUserName.isBlank() && currentPwd != null && !currentPwd.isBlank();
        if (isValid) {
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("username or password should not be empty");
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}
