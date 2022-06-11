package ru.rumal.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profile.active:prod}")
    private String profile;

    @GetMapping
    public String getIndex(Model model) {

        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
}
