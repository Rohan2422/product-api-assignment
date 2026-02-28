package com.zest.productapi.security;

import org.springframework.web.bind.annotation.RequestMapping;

import com.zest.productapi.dto.RefreshRequest;
import com.zest.productapi.entity.RefreshToken;
import com.zest.productapi.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Transactional
	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		String accessToken = jwtUtil.generateToken(request.getUsername());
		String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

		refreshTokenRepository.deleteByUsername(request.getUsername());

		RefreshToken tokenEntity = RefreshToken.builder().token(refreshToken).username(request.getUsername())
				.expiryDate(LocalDateTime.now().plusDays(7)).build();

		refreshTokenRepository.save(tokenEntity);

		return new AuthResponse(accessToken, refreshToken);
	}

	@PostMapping("/refresh")
	public AuthResponse refresh(@RequestBody RefreshRequest request) {

		String refreshToken = request.getRefreshToken();

		RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));

		if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
			refreshTokenRepository.delete(tokenEntity);
			throw new RuntimeException("Refresh token expired");
		}

		String username = tokenEntity.getUsername();

		refreshTokenRepository.delete(tokenEntity);

		String newAccessToken = jwtUtil.generateToken(username);
		String newRefreshToken = jwtUtil.generateRefreshToken(username);

		RefreshToken newTokenEntity = RefreshToken.builder().token(newRefreshToken).username(username)
				.expiryDate(LocalDateTime.now().plusDays(7)).build();

		refreshTokenRepository.save(newTokenEntity);

		return new AuthResponse(newAccessToken, newRefreshToken);
	}
}
