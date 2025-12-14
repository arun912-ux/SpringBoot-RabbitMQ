package org.example.rabbitmqdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class WebController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("home")
    @ResponseBody
    public String home() {
        return "home";
    }


}
