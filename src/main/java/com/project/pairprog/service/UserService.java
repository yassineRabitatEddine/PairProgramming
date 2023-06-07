package com.project.pairprog.service;

import com.google.common.hash.Hashing;
import com.project.pairprog.model.User;
import com.project.pairprog.model.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUsernameAndPassword(String username, String password) {
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return userRepository.findByUsernameAndPassword(username, hashedPassword);
    }

    @Transactional
    public int deleteByUsernameAndPassword(String username, String password) {
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return userRepository.deleteByUsernameAndPassword(username, hashedPassword);
    }

    @Query("update User u set u.username = ?1, u.email=?2, u.password=?3 where u.username=?4")
    @Modifying
    @Transactional
    public int updateUser(String newUsername, String email, String password, String oldUsername) {
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return userRepository.updateUser(newUsername, email, hashedPassword, oldUsername);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public int save(User user) {
        if (findByUsername(user.getUsername())!=null) return 0;
        else {
            String hashedPassword = Hashing.sha256()
                    .hashString(user.getPassword(), StandardCharsets.UTF_8)
                    .toString();
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return 1;
        }
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
