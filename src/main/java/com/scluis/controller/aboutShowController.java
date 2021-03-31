package com.scluis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sichengluis on 2021/2/10 14:27
 */
@Controller
@RequestMapping("/about")
public class aboutShowController {
    @GetMapping("/")
    public String about(){
        return "about";
    }
}
