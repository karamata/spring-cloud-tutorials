package com.edinnova.spring.cloud.example.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleResource {
	@RequestMapping("/greeting")
	public String greeting() {
		return "Hello from EurekaClient!";
	}
}
