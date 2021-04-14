package com.example.task5.controller;

import com.example.task5.repo.UserRepo;
import com.example.task5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getUserPage(Model model) {

        if (userService.checkCurrentUserSession(SecurityContextHolder
                .getContext().getAuthentication().getName())) {
            return "redirect:/user";
        }


        model.addAttribute("users",userRepo.findAll());
        model.addAttribute("username",SecurityContextHolder.getContext()
                .getAuthentication().getName());

        return "user";
    }

    @PostMapping("/changedata")
    public String changeUserData(@RequestParam(required = false) ArrayList<String> id, String action) {
        userService.checkCurrentUserSession(SecurityContextHolder
                .getContext().getAuthentication().getName());

        if (id != null) {
            if (action.equals("block")) {
                userService.changeUserStatus(id, false);
            } else if (action.equals("unblock")) {
                userService.changeUserStatus(id, true);
            } else if (action.equals("delete")) {
                userService.deleteUser(id);
            }
        }

        return "redirect:/user";
    }
}
