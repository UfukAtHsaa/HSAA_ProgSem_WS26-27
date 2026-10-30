package com.example.hello.broker;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/** Ein Wertpapier, das man kaufen kann. Den Preis kennt nur der Server. */
@Entity
public class Instrument {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String symbol;

	private String name;

	private BigDecimal price;

	protected Instrument() {
		// für JPA
	}

	public Instrument(String symbol, String name, BigDecimal price) {
		this.symbol = symbol;
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
