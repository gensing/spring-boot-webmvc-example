
package com.tensing.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HomeController {

    @GetMapping(value = {"", "/"})
    public String home() {
        return "hello";
    }

}