package com.example.hello.exchangerate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Eine winzige Fake-API für Tests, gebaut mit dem HTTP-Server aus dem JDK.
 * Sie kann gut antworten, langsam sein oder einen Fehler liefern.
 */
public class FakeFrankfurterApi {

	public enum Mode { OK, SLOW, ERROR }

	private final HttpServer server;
	private volatile Mode mode = Mode.OK;

	public FakeFrankfurterApi() {
		try {
			server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		server.createContext("/v1/latest", this::handle);
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}

	public String baseUrl() {
		return "http://localhost:" + server.getAddress().getPort();
	}

	public void mode(Mode mode) {
		this.mode = mode;
	}

	private void handle(HttpExchange exchange) throws IOException {
		switch (mode) {
			case OK -> send(exchange, 200, "{\"amount\":1.0,\"base\":\"EUR\",\"date\":\"2026-11-24\",\"rates\":{\"USD\":1.1}}");
			case ERROR -> send(exchange, 500, "{\"message\":\"kaputt\"}");
			case SLOW -> {
				sleep(3000); // viel länger als der read-timeout im Test
				send(exchange, 200, "{\"amount\":1.0,\"base\":\"EUR\",\"date\":\"2026-11-24\",\"rates\":{\"USD\":1.1}}");
			}
		}
	}

	private static void send(HttpExchange exchange, int status, String json) throws IOException {
		byte[] body = json.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().add("Content-Type", "application/json");
		exchange.sendResponseHeaders(status, body.length);
		exchange.getResponseBody().write(body);
		exchange.close();
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
