package by.teachmeskills.shopwebservice.services;

import by.teachmeskills.shopwebservice.dto.AuthResponse;
import by.teachmeskills.shopwebservice.dto.RefreshJwtTokenDto;
import by.teachmeskills.shopwebservice.dto.UserCredentialsRequest;
import jakarta.security.auth.message.AuthException;

public interface AuthService {
    AuthResponse login(UserCredentialsRequest request) throws AuthException;
    AuthResponse getRefreshToken(RefreshJwtTokenDto refreshTokenRequest) throws AuthException;
    AuthResponse getAccessToken(RefreshJwtTokenDto refreshTokenRequest);
}
