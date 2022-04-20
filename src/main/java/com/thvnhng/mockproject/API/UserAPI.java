package com.thvnhng.mockproject.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users")
public class UserAPI {

    @GetMapping("/hel")
    public String hello() {
        return "hello";
    }
}
