package com.example.hello;

import java.time.Instant;

public record Greeting(String message, Instant servedAt) {
}