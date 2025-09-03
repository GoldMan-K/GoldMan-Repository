package com.community.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@CrossOrigin(origins = "http://localhost:8080") // Vue 서버의 기본 포트
public class HomeController {

    @GetMapping("/index")
    public String home() {
        return "forward:/index.html";
    }
}
