package com.example.hello.broker;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.hello.security.AppUser;
import com.example.hello.security.AppUserRepository;

/**
 * Beispieldaten beim ersten Start: drei Wertpapiere, ein Admin, eine Kundin.
 * Die Passwörter kommen aus Umgebungsvariablen (.env), nie aus dem Code.
 */
@Component
public class DemoData implements CommandLineRunner {

	private final InstrumentRepository instruments;
	private final AppUserRepository users;
	private final PasswordEncoder encoder;
	private final String adminPassword;
	private final String kundePassword;

	public DemoData(InstrumentRepository instruments, AppUserRepository users, PasswordEncoder encoder,
			@Value("${broker.admin-password}") String adminPassword,
			@Value("${broker.kunde-password}") String kundePassword) {
		this.instruments = instruments;
		this.users = users;
		this.encoder = encoder;
		this.adminPassword = adminPassword;
		this.kundePassword = kundePassword;
	}

	@Override
	public void run(String... args) {
		if (instruments.count() == 0) {
			instruments.save(new Instrument("SAP", "SAP SE", new BigDecimal("231.40")));
			instruments.save(new Instrument("SIE", "Siemens AG", new BigDecimal("198.75")));
			instruments.save(new Instrument("ALV", "Allianz SE", new BigDecimal("312.10")));
		}
		if (users.count() == 0) {
			users.save(new AppUser("admin@broker.local", encoder.encode(adminPassword), "ADMIN"));
			users.save(new AppUser("anna@example.org", encoder.encode(kundePassword), "KUNDE"));
		}
	}
}
