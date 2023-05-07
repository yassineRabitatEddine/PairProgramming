package com.project.pairprog.controller;

import com.project.pairprog.model.User;
import com.project.pairprog.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    UserAuth userAuth;

    @GetMapping("/register")
    public String register(Model model){
        User employe=new User();
        model.addAttribute("User",employe);
        return "register";
    }

    @PostMapping("/register")

    public String register(@Valid @ModelAttribute("User") User user, BindingResult result){
        if(result.hasErrors()) return "register";
        else if (userService.save(user)==0) return "failedReg";
        else return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("User",new User() );
        return "login";
    }
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("User")UserAuth userAuth1, BindingResult result){
        if(result.hasErrors()) return "login";
        else if (userService.findByUsernameAndPassword(userAuth1.getUsername(),userAuth1.getPassword())==null)return "failedLogin";
        else return "redirect:/userAccount/"+userAuth1.getUsername();
    }

    @GetMapping("/userAccount/listUsers/{username}")
    public String listUsers(@PathVariable("username") String username,Model model){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        return "listUsers";
    }

    @PostMapping("/userAccount/listUsers/{username}")
    public String listUsers(@PathVariable("username")String username,@RequestParam(name = "username1")String username1,Model model){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        model.addAttribute("User1",userService.findByUsername(username1));
        return "listUsersPost";
    }

    @GetMapping("/userAccount/{username}")
    public String userAccount(@PathVariable("username") String username, Model model){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        return "userAccount";
    }

    @GetMapping("/userAccount/deleteAccount/{username}")
    public String deleteAccount(@PathVariable("username") String username,Model model){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        return "deleteAccount";
    }

   @PostMapping("/userAccount/deleteAccount/{username}")
    public String deleteAccount(@PathVariable("username") String username,@RequestParam("password")String password,Model model){
       User user=userService.findByUsername(username);
       User user1=userService.findByUsernameAndPassword(username,password);
        model.addAttribute("User",user);
        model.addAttribute("User1",user1);
        if (user1==null) return "deleteAccountPost";
        else{
            userService.deleteByUsernameAndPassword(user1.getUsername(),user1.getPassword());
            return "login";
        }
    }

    @GetMapping("/userAccount/updateUser/{username}")
    public String updateUser(@PathVariable("username") String username,Model model){
        User user=userService.findByUsername(username);
        model.addAttribute("User",user);
        return "updateUser";
    }

    @PostMapping("/userAccount/updateUser/{username}")
    public String updateUser(@PathVariable("username") String username, @Valid @ModelAttribute("User") User user, BindingResult result, Model model){
        if(result.hasErrors()) return "updateUser";
        else {
            model.addAttribute("updateReturn",userService.updateUser(user.getUsername(),user.getEmail(),user.getPassword(),username));
            return "modifySuccess";
        }
    }
}
