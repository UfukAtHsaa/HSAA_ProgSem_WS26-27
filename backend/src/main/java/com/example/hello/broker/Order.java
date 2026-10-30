package com.example.hello.broker;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity: so liegt eine Order in der Datenbank.
 * "order" ist in SQL ein reserviertes Wort, darum heißt die Tabelle "orders".
 */
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private String symbol;

	private int quantity;

	private BigDecimal price;

	/** Internes Feld: geht nie an den Client (siehe OrderDto). */
	private String internalNote;

	private Long ownerId;

	protected Order() {
		// für JPA
	}

	public Order(String symbol, int quantity, BigDecimal price, Long ownerId) {
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
		this.ownerId = ownerId;
	}

	public Long getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getInternalNote() {
		return internalNote;
	}

	public void setInternalNote(String internalNote) {
		this.internalNote = internalNote;
	}

	public Long getOwnerId() {
		return ownerId;
	}
}
