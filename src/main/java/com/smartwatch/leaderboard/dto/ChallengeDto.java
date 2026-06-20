package com.smartwatch.leaderboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

public class ChallengeDto {
    private Long id;

    @NotBlank(message = "Challenge name is required")
    private String name;

    private String description;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;

    private Set<String> requiredFeatureTags;

    @NotBlank(message = "Scope type is required (e.g. GLOBAL, CITY, RADIUS)")
    private String scopeType;

    private String city;

    private Double latitude;

    private Double longitude;

    private Double radiusKm;

    public ChallengeDto() {
    }

    public ChallengeDto(Long id, String name, String description, LocalDateTime expiryDate, Set<String> requiredFeatureTags, String scopeType, String city, Double latitude, Double longitude, Double radiusKm) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expiryDate = expiryDate;
        this.requiredFeatureTags = requiredFeatureTags;
        this.scopeType = scopeType;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusKm = radiusKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Set<String> getRequiredFeatureTags() {
        return requiredFeatureTags;
    }

    public void setRequiredFeatureTags(Set<String> requiredFeatureTags) {
        this.requiredFeatureTags = requiredFeatureTags;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadiusKm() {
        return radiusKm;
    }

    public void setRadiusKm(Double radiusKm) {
        this.radiusKm = radiusKm;
    }
}
