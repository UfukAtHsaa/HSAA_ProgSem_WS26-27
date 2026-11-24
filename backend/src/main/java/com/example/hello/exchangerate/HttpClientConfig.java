package com.example.hello.exchangerate;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * Spring baut aus dem Interface einen fertigen Client (eine Bean).
 * Adresse und Timeouts stehen in application.properties unter
 * spring.http.serviceclient.frankfurter.*
 */
@Configuration
@ImportHttpServices(group = "frankfurter", types = FrankfurterClient.class)
public class HttpClientConfig {
}
