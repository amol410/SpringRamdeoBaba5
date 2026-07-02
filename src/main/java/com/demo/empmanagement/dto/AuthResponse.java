package com.demo.empmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder                // Generates: builder pattern (AuthResponse.builder().accessToken("...").build())
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    // =====================================================================
    // LOMBOK replaces ALL of the following boilerplate code (commented out):
    // =====================================================================

    // public AuthResponse() {}
    //
    // public AuthResponse(String accessToken, String refreshToken) {
    //     this.accessToken = accessToken;
    //     this.refreshToken = refreshToken;
    // }
    //
    // public String getAccessToken() { return accessToken; }
    // public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    // public String getRefreshToken() { return refreshToken; }
    // public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    //
    // public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    //
    // public static class AuthResponseBuilder {
    //     private String accessToken;
    //     private String refreshToken;
    //     public AuthResponseBuilder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
    //     public AuthResponseBuilder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
    //     public AuthResponse build() { return new AuthResponse(accessToken, refreshToken); }
    // }
}
