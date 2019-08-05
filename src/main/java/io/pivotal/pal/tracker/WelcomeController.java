package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private String any_varaible;

    public WelcomeController(@Value("${welcome.message}") String message){
        this.any_varaible = message;
    }

    @GetMapping("/")
    public String sayHello() {
        return any_varaible;
    }
}