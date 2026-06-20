package com.smartwatch.leaderboard.dto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Manual Builder Pattern
    public static UserLoginDtoBuilder builder() {
        return new UserLoginDtoBuilder();
    }

    public static class UserLoginDtoBuilder {
        private String phone;
        private String password;

        public UserLoginDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserLoginDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserLoginDto build() {
            return new UserLoginDto(phone, password);
        }
    }
}