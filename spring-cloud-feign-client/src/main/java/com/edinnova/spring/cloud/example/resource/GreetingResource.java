package com.edinnova.spring.cloud.example.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edinnova.spring.cloud.example.client.GreetingClient;

@RestController
public class GreetingResource {
	@Autowired
    private GreetingClient greetingClient;
	
	@RequestMapping("/get-greeting")
    public String greeting(Model model) {
        return "greeting with message : " + greetingClient.greeting();
    }
}
