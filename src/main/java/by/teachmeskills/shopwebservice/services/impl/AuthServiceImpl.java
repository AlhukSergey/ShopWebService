package by.teachmeskills.shopwebservice.services.impl;

import by.teachmeskills.shopwebservice.config.JwtProvider;
import by.teachmeskills.shopwebservice.dto.AuthResponse;
import by.teachmeskills.shopwebservice.dto.RefreshJwtTokenDto;
import by.teachmeskills.shopwebservice.dto.UserCredentialsRequest;
import by.teachmeskills.shopwebservice.entities.User;
import by.teachmeskills.shopwebservice.services.AuthService;
import by.teachmeskills.shopwebservice.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public AuthServiceImpl(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResponse login(UserCredentialsRequest request) throws AuthException {
        User user = userService.getByEmailAndPassword(request.getLogin(), request.getPassword());

        if (user != null) {
            String token = jwtProvider.generateAccessToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
            refreshStorage.put(user.getEmail(), refreshToken);
            return new AuthResponse(String.format("Token: %1$s and refresh token: %2$s", token, refreshToken));
        }
        throw new AuthException("Неправильный пароль");
    }

    public AuthResponse getAccessToken(RefreshJwtTokenDto refreshTokenRequest) {
        if (jwtProvider.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
           Claims claims = jwtProvider.getRefreshClaims(refreshTokenRequest.getRefreshToken());
           String login = claims.getSubject();
           String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshTokenRequest.getRefreshToken())) {
                User user = userService.getByEmail(login);
                String accessToken = jwtProvider.generateAccessToken(user.getEmail());
                return new AuthResponse(accessToken);
            }
        }
        return new AuthResponse(null);
    }

    public AuthResponse getRefreshToken(RefreshJwtTokenDto refreshTokenRequest) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshTokenRequest.getRefreshToken());
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshTokenRequest.getRefreshToken())) {
                User user = userService.getByEmail(login);
                String accessToken = jwtProvider.generateAccessToken(user.getEmail());
                String newRefreshToken = jwtProvider.generateRefreshToken(user.getEmail());
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new AuthResponse(String.format("Token: %1$s and refresh token: %2$s", accessToken, newRefreshToken));
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
