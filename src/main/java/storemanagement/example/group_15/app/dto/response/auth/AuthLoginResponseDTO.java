package storemanagement.example.group_15.app.dto.response.auth;

import storemanagement.example.group_15.domain.auth.constant.Role;

public class AuthLoginResponseDTO {
    private final String accessToken;
    private final String refreshToken;

    public AuthLoginResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
