package com.example.hello;

import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GreetingController {

	@GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
	public Greeting hello() {
		return new Greeting("Hello, World!", Instant.now());
	}
}