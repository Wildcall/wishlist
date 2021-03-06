package ru.rumal.wishlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class MainController implements ErrorController {

    @Value("${spring.profile.active:prod}")
    private String profile;

    @GetMapping
    public String getIndex(Principal principal,
                           Model model,
                           @RequestParam(required = false) String token) {
        model.addAttribute("isDevMode", "dev".equals(profile));
        model.addAttribute("auth", principal != null);
        if (token != null)
            model.addAttribute("reserveToken", token);
        return "index";
    }

    @RequestMapping("/error")
    public String error() {
        return "redirect:/";
    }
}
