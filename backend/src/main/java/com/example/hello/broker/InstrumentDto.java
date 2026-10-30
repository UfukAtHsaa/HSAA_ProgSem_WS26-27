package com.example.hello.broker;

import java.math.BigDecimal;

public record InstrumentDto(String symbol, String name, BigDecimal price) {

	static InstrumentDto from(Instrument i) {
		return new InstrumentDto(i.getSymbol(), i.getName(), i.getPrice());
	}
}
