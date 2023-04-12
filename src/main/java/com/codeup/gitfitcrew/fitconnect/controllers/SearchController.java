package com.codeup.gitfitcrew.fitconnect.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    @Value("${google-maps-api-key}")
    private String googleMapsApiKey;

    @GetMapping("/search")
    public String showSearchPage(Model model){
        model.addAttribute("apiKey", googleMapsApiKey);
        return "search";
    }
}