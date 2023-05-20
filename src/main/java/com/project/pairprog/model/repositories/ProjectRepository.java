package com.project.pairprog.model.repositories;

import com.project.pairprog.model.Project;
import com.project.pairprog.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    public Project findByName(String name);
    public int deleteByName(String name);
    @Transactional
    @Modifying
    @Query("update Project p set p.name = ?1 where p.name=?2")
    int updateProject(String newName, String oldName);
}
