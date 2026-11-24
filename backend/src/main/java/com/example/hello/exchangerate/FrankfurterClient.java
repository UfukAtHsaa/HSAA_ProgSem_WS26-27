package com.example.hello.exchangerate;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Die fremde API als Java-Interface. Eine Implementierung schreiben wir nicht,
 * die baut Spring (siehe HttpClientConfig). Gleiche Idee wie früher @FeignClient.
 *
 * Aufruf: GET https://api.frankfurter.dev/v1/latest?base=EUR&symbols=USD
 */
@HttpExchange("/v1")
public interface FrankfurterClient {

	@GetExchange("/latest")
	FrankfurterRates latest(@RequestParam String base, @RequestParam String symbols);
}
