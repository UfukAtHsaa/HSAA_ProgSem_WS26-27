package com.example.hello.broker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/** Was der Client schicken darf. Kein Preis: den bestimmt der Server. */
public record CreateOrderRequest(@NotBlank String symbol, @Positive int quantity) {
}
