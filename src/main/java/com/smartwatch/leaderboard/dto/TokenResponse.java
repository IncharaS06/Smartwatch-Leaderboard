package com.smartwatch.leaderboard.dto;

public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String fullName;
    private String role;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken, String refreshToken, Long userId, String fullName, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Manual Builder Pattern
    public static TokenResponseBuilder builder() {
        return new TokenResponseBuilder();
    }

    public static class TokenResponseBuilder {
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private String fullName;
        private String role;

        public TokenResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public TokenResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public TokenResponseBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public TokenResponseBuilder role(String role) {
            this.role = role;
            return this;
        }

        public TokenResponse build() {
            return new TokenResponse(accessToken, refreshToken, userId, fullName, role);
        }
    }
}
