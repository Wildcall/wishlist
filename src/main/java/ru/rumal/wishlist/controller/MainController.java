package ru.rumal.wishlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profile.active:prod}")
    private String profile;

    @GetMapping
    public String getIndex(@AuthenticationPrincipal OAuth2User userPrincipal,
                           Model model) {
        log.info("User info: {}", userPrincipal);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
}
