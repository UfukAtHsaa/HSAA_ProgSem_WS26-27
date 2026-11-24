package com.example.hello.broker;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Öffentlich: Wertpapiere ansehen darf jeder, auch ohne Login (siehe SecurityConfig). */
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

	private final InstrumentRepository instruments;
	private final QuoteService quotes;

	public InstrumentController(InstrumentRepository instruments, QuoteService quotes) {
		this.instruments = instruments;
		this.quotes = quotes;
	}

	@GetMapping
	public PageResponse<InstrumentDto> list(Pageable pageable) {
		return PageResponse.from(instruments.findAll(pageable).map(InstrumentDto::from));
	}

	@GetMapping("/{symbol}")
	public InstrumentDto get(@PathVariable String symbol) {
		return instruments.findBySymbol(symbol)
			.map(InstrumentDto::from)
			.orElseThrow(() -> new NotFoundException("Wertpapier " + symbol + " gibt es nicht."));
	}

	/** Preis in anderer Währung, Kurs von einer externen API. Beispiel: /api/instruments/SAP/quote?currency=USD */
	@GetMapping("/{symbol}/quote")
	public QuoteDto quote(@PathVariable String symbol, @RequestParam(defaultValue = "USD") String currency) {
		return quotes.quote(symbol, currency);
	}
}
