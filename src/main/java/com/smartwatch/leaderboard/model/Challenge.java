package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "challenge_required_tags", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "required_tag")
    private Set<String> requiredFeatureTags = new HashSet<>();

    @Column(name = "scope_type", nullable = false)
    private String scopeType; // GLOBAL, CITY, RADIUS

    private String city;

    private Double latitude;

    private Double longitude;

    @Column(name = "radius_km")
    private Double radiusKm;

    public Challenge() {
    }

    public Challenge(Long id, String name, String description, LocalDateTime expiryDate, Set<String> requiredFeatureTags, String scopeType, String city, Double latitude, Double longitude, Double radiusKm) {
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

    // Manual Builder
    public static ChallengeBuilder builder() {
        return new ChallengeBuilder();
    }

    public static class ChallengeBuilder {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime expiryDate;
        private Set<String> requiredFeatureTags = new HashSet<>();
        private String scopeType;
        private String city;
        private Double latitude;
        private Double longitude;
        private Double radiusKm;

        public ChallengeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ChallengeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ChallengeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ChallengeBuilder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public ChallengeBuilder requiredFeatureTags(Set<String> requiredFeatureTags) {
            this.requiredFeatureTags = requiredFeatureTags;
            return this;
        }

        public ChallengeBuilder scopeType(String scopeType) {
            this.scopeType = scopeType;
            return this;
        }

        public ChallengeBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ChallengeBuilder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public ChallengeBuilder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public ChallengeBuilder radiusKm(Double radiusKm) {
            this.radiusKm = radiusKm;
            return this;
        }

        public Challenge build() {
            return new Challenge(id, name, description, expiryDate, requiredFeatureTags, scopeType, city, latitude, longitude, radiusKm);
        }
    }
}
