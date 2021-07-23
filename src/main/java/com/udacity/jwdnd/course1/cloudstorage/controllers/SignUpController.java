package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signUpView() {
        return "signup";
    }

    @PostMapping
    public String signUpUser(@ModelAttribute User user, Model model) {
        String signUpError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signUpError = "Username already exists.";
        }

        if (signUpError == null) {
            int rowsAdded = userService.createUser(user);

            if (rowsAdded < 0) {
                signUpError = "Error while writing to database.";
            }
        }

        if (signUpError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signUpError);
        }

        return "signup";
    }
}
