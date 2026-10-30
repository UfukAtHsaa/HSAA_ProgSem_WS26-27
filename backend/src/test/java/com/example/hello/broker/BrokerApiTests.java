package com.example.hello.broker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Die ganze App mit H2 im Speicher. Dieselben Fälle wie in http/broker.http,
 * nur automatisch bei jedem Build.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BrokerApiTests {

	private static final String ANNA = "anna@example.org";
	private static final String ADMIN = "admin@broker.local";

	@Autowired
	private MockMvc mvc;

	@Test
	void instrumentsArePublic() throws Exception {
		mvc.perform(get("/api/instruments"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalItems").value(3));
	}

	@Test
	void adminAreaWithoutLoginIs401() throws Exception {
		mvc.perform(get("/api/admin/orders"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	void adminAreaAsCustomerIs403() throws Exception {
		mvc.perform(get("/api/admin/orders").with(httpBasic(ANNA, "kunde-test")))
			.andExpect(status().isForbidden());
	}

	@Test
	void adminAreaAsAdminIs200() throws Exception {
		mvc.perform(get("/api/admin/orders").with(httpBasic(ADMIN, "admin-test")))
			.andExpect(status().isOk());
	}

	@Test
	void placeOrderReturns201WithLocation() throws Exception {
		mvc.perform(post("/api/orders").with(httpBasic(ANNA, "kunde-test"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"symbol\": \"SAP\", \"quantity\": 2}"))
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(jsonPath("$.price").value(231.40))
			.andExpect(jsonPath("$.internalNote").doesNotExist());
	}

	@Test
	void invalidQuantityIs400AsProblemDetails() throws Exception {
		mvc.perform(post("/api/orders").with(httpBasic(ANNA, "kunde-test"))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"symbol\": \"SAP\", \"quantity\": 0}"))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
	}

	@Test
	void unknownOrderIs404AsProblemDetails() throws Exception {
		mvc.perform(get("/api/orders/999999").with(httpBasic(ANNA, "kunde-test")))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
	}
}
