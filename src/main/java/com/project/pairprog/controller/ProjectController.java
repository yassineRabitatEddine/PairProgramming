package com.project.pairprog.controller;

import com.project.pairprog.model.Project;
import com.project.pairprog.model.User;
import com.project.pairprog.service.ProjectService;
import com.project.pairprog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    public ProjectService projectService;
    @Autowired
    public UserService userService;

    @GetMapping("/userAccount/projects/addProject/{username}")
    public String addProject(@PathVariable("username") String username, Model model){
        User user=userService.findByUsername(username);
        Project project=new Project();
        model.addAttribute("User",user);
        model.addAttribute("Project",project);
        return "addProject";
    }

    @PostMapping("/userAccount/projects/addProject/{username}")
    public String addProject(@Valid @ModelAttribute("Project") Project project,@PathVariable("username") String username, BindingResult result, Model model){
        if (result.hasErrors()) return "addProject";
        else{
            int r=projectService.saveProject(project.getName(),username);
            model.addAttribute("exist",r);
            model.addAttribute("User",userService.findByUsername(username));
            model.addAttribute("projectName",project.getName());
            if(r==0) return "addProject";
            else {
                return "redirect:/userAccount/projects/listProjects/"+username;
            }
        }
    }

    @GetMapping("/userAccount/projects/addCollaborator/{username}/{username1}")
    public String addCollaborator(@PathVariable("username") String username, Model model,@PathVariable("username1")String username1){
        model.addAttribute("User",userService.findByUsername(username));
        model.addAttribute("username1",username1);
        return "addCollaborator";
    }

    @PostMapping("/userAccount/projects/addCollaborator/{username}/{username1}")
    public String addCollaborator(@PathVariable("username") String username,@PathVariable("username1")String username1, Model model,@RequestParam("projectName")String projectName){
        User user=userService.findByUsername(username);
        User user1=userService.findByUsername(username1);
        Project project=projectService.findByName(projectName);
        if (project==null){
            model.addAttribute("exists",0);
            model.addAttribute("User",user);
            model.addAttribute("projectOwned",true);
            return "addCollaboratorPost";
            }
        else {
            if(!user.getProjectsOwned().contains(project)){
                model.addAttribute("projectOwned",false);
                model.addAttribute("User",user);
                model.addAttribute("ProjectCollab",1);
                return "addCollaboratorPost";
            }
            else if (project.getCollaborators().contains(user1)) {
                model.addAttribute("projectOwned",true);
                model.addAttribute("User",user);
                model.addAttribute("ProjectCollab",0);
                return "addCollaboratorPost";
            } else {
                model.addAttribute("exists",1);
                model.addAttribute("projectOwned",true);
                Project project1=projectService.addCollaborator(user1,project);
                model.addAttribute("returnValue",project1);
                model.addAttribute("ProjectCollab",1);
            }
            model.addAttribute("User",user);
            return "addCollaboratorPost";
            }
    }

    @GetMapping("/userAccount/projects/listProjects/{username}")
    public String listProjects(@PathVariable("username") String username,Model model){
        List<Project> projects= userService.findByUsername(username).getProjectsOwned();
        model.addAttribute("Projects",projects);
        model.addAttribute("User",userService.findByUsername(username));
        return "listProjects";
    }

    @GetMapping("/userAccount/projects/deleteProject/{username}/{projectName}")
    public String deleteProject(@PathVariable("username")String username, Model model,@PathVariable("projectName") String projectName){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        model.addAttribute("Projects",user.getProjectsOwned());
        projectService.deleteByName(projectName);
        return "listProjects";
        }

        @GetMapping("/userAccount/projects/findProject/{username}")
    public String findProject(@PathVariable("username")String username, Model model){
        model.addAttribute("User",userService.findByUsername(username));
        return "findProject";
        }

        @PostMapping("/userAccount/projects/findProject/{username}")
    public String findProject(@PathVariable("username")String username, @RequestParam("projectName")String projectName, Model model){
        model.addAttribute("User",userService.findByUsername(username));
        model.addAttribute("Project",projectService.findByName(projectName));
        return "findProjectPost";
        }

        @GetMapping("/userAccount/projects/joinProject/{username}/{projectName}")
    public String joinProject(@PathVariable("username")String username, Model model,@PathVariable("projectName")String projectName){
        model.addAttribute("User",userService.findByUsername(username));
        model.addAttribute("Project",projectService.findByName(projectName));
        return "joinProject";
        }

        @PostMapping("/userAccount/projects/joinProject/{username}/{projectName}")
    public String joinProject(@PathVariable("username")String username, Model model,@PathVariable("projectName")String projectName,@RequestParam("token")Integer token){
        Project project=projectService.findByName(projectName);
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        model.addAttribute("Project",project);
        int r;
        if (!token.equals(project.getProject_token())){
            r=0;
            model.addAttribute("equal",r);
            model.addAttribute("ProjectCollab",1);
            return "joinProjectPost";

        } else if (project.getCollaborators().contains(user)) {
            model.addAttribute("User",user);
            model.addAttribute("Project",project);
            model.addAttribute("ProjectCollab",0);
            return "joinProjectPostFailed";
        } else{
            r=1;
            model.addAttribute("equal",r);
            projectService.addCollaborator(userService.findByUsername(username),project);
            return "joinProjectPost";
        }
        }

        @GetMapping("/userAccount/projects/updateProject/{username}/{projectName}")
    public String updateProject(@PathVariable("username")String username,@PathVariable("projectName")String projectName,Model model){
        User user=userService.findByUsername(username);
        Project project=projectService.findByName(projectName);
        model.addAttribute("User",user);
        model.addAttribute("Project",project);
        return "updateProject";
        }

        @PostMapping("/userAccount/projects/updateProject/{username}/{projectName}")
    public String updateProject(@PathVariable("username") String username,@PathVariable("projectName") String projectName, @RequestParam("projectName")String projectName1,Model model){
        model.addAttribute("returnValue",projectService.updateProject(projectName1,projectName));
        model.addAttribute("User",userService.findByUsername(username));
        model.addAttribute("Project",projectService.findByName(projectName1));
        return "updateProjectPost";
        }
    }

