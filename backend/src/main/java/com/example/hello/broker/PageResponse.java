package com.example.hello.broker;

import java.util.List;

import org.springframework.data.domain.Page;

/** Eigenes Seitenformat aus Vorlesung 1, statt Springs Page direkt auszuliefern. */
public record PageResponse<T>(List<T> items, int page, int size, long totalItems, int totalPages) {

	static <T> PageResponse<T> from(Page<T> p) {
		return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
	}
}
