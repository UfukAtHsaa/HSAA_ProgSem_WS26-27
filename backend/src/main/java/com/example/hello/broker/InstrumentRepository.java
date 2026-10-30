package com.example.hello.broker;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

	Optional<Instrument> findBySymbol(String symbol);
}
