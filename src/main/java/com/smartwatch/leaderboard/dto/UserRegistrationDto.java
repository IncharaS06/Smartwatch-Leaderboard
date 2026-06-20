package com.smartwatch.leaderboard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String phone, String email, String fullName, String password) {
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Manual Builder Pattern
    public static UserRegistrationDtoBuilder builder() {
        return new UserRegistrationDtoBuilder();
    }

    public static class UserRegistrationDtoBuilder {
        private String phone;
        private String email;
        private String fullName;
        private String password;

        public UserRegistrationDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserRegistrationDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserRegistrationDtoBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserRegistrationDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserRegistrationDto build() {
            return new UserRegistrationDto(phone, email, fullName, password);
        }
    }
}