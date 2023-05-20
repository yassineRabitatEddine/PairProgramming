package com.project.pairprog.service;

import com.project.pairprog.model.Project;
import com.project.pairprog.model.User;
import com.project.pairprog.model.repositories.ProjectRepository;
import com.project.pairprog.model.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ProjectService {

     @Autowired
    public ProjectRepository projectRepository;
     @Autowired
    public UserRepository userRepository;

    public Project findByName(String name) {
        return projectRepository.findByName(name);
    }

    @Transactional
    public int deleteByName(String name) {
        return projectRepository.deleteByName(name);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public int saveProject(String projectName,String username) {
        Project project1=new Project();
        User user=userRepository.findByUsername(username);
        project1.setName(projectName);
        project1.setOwner(user);
        user.getProjectsOwned().add(project1);
        Random random=new Random();
        int r=random.nextInt(10000);
        project1.setProject_token(r);
        if (findByName(project1.getName())!=null) return 0;
        else {
            projectRepository.save(project1);
            return 1;
        }
    }

    public int updateProject(String newName,String oldName){
        return projectRepository.updateProject(newName,oldName);
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }

    @Transactional
    public Project addCollaborator(User user, Project project){
        project.getCollaborators().add(user);
        return projectRepository.save(project);
    }
}
