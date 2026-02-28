package com.zest.productapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.repository.RefreshTokenRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@BeforeEach
	void clearTokens() {
		refreshTokenRepository.deleteAll();
	}

	// ================= LOGIN TEST =================

	@Test
	void login_ShouldReturnAccessToken() throws Exception {

		String loginRequest = """
				{
				  "username": "admin",
				  "password": "admin"
				}
				""";

		mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginRequest))
				.andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").exists());
	}

	// ================= CREATE PRODUCT TEST =================

	@Test
	void createProduct_ShouldReturnCreated() throws Exception {

		String token = obtainAccessToken();

		ProductRequest request = new ProductRequest();
		request.setProductName("Integration Laptop");
		request.setCreatedBy("admin");

		mockMvc.perform(post("/api/v1/products").header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.productName").value("Integration Laptop"));
	}

	// ================= GET PRODUCT TEST =================

	@Test
	void getProduct_ShouldReturnProduct() throws Exception {

		String token = obtainAccessToken();

		ProductRequest request = new ProductRequest();
		request.setProductName("Phone");
		request.setCreatedBy("admin");

		String response = mockMvc
				.perform(post("/api/v1/products").header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andReturn().getResponse().getContentAsString();

		Integer id = objectMapper.readTree(response).get("id").asInt();

		mockMvc.perform(get("/api/v1/products/" + id).header("Authorization", "Bearer " + token))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
	}

	// ================= HELPER METHOD =================

	private String obtainAccessToken() throws Exception {

		String loginRequest = """
				{
				  "username": "admin",
				  "password": "admin"
				}
				""";

		String response = mockMvc
				.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginRequest))
				.andReturn().getResponse().getContentAsString();

		return objectMapper.readTree(response).get("accessToken").asText();
	}
}