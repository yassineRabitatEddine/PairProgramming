package com.project.pairprog.model.repositories;

import com.project.pairprog.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUsernameAndPassword(String username, String password);
    public int deleteByUsernameAndPassword(String username, String password);
    public User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.username = ?1, u.email=?2, u.password=?3 where u.username=?4")
    int updateUser(String newUsername, String email, String password, String oldUsername);



}
