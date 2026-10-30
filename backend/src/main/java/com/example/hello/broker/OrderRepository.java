package com.example.hello.broker;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Nur ein Interface. Die Klasse dazu baut Spring Data beim Start.
 * Geschenkt: findById, findAll, save, deleteById, count …
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/** Der Name ist die Abfrage: WHERE owner_id = ? */
	List<Order> findByOwnerId(Long ownerId);

	/** WHERE symbol = ?, dazu page, size und sort aus der URL. */
	Page<Order> findBySymbol(String symbol, Pageable pageable);
}
