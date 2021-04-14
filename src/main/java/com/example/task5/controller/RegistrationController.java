package com.example.task5.controller;

import com.example.task5.model.User;
import com.example.task5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "registration";
        } else if (!userService.saveUser(user)) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Such user already exists!");
            return "registration";
        }
        return "redirect:/";
    }
}
