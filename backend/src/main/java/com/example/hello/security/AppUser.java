package com.example.hello.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/** Ein Benutzer in unserer Tabelle. Gespeichert wird nur der BCrypt-Hash. */
@Entity
public class AppUser {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	private String passwordHash;

	/** "ADMIN" oder "KUNDE". Spring macht daraus ROLE_ADMIN bzw. ROLE_KUNDE. */
	private String role;

	protected AppUser() {
		// für JPA
	}

	public AppUser(String email, String passwordHash, String role) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getRole() {
		return role;
	}
}
