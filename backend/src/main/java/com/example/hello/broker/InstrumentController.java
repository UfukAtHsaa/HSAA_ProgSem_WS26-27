package com.example.hello.broker;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Öffentlich: Wertpapiere ansehen darf jeder, auch ohne Login (siehe SecurityConfig). */
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

	private final InstrumentRepository instruments;

	public InstrumentController(InstrumentRepository instruments) {
		this.instruments = instruments;
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
}
