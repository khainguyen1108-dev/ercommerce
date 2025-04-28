package storemanagement.example.group_15.app.dto.response.auth;

import storemanagement.example.group_15.domain.users.constant.Role;

public class AuthLoginResponseDTO {
    private final String accessToken;
    private final String refreshToken;
    private final Role role;

    public AuthLoginResponseDTO(String accessToken, String refreshToken, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;

    }
    public Role getRole(){
        return this.role;
    }
    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
