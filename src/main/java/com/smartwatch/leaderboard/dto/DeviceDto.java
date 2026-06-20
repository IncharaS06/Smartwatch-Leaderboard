package com.smartwatch.leaderboard.dto;

import java.util.Set;

public class DeviceDto {
    private Long id;
    private String name;
    private String brand;
    private Set<String> featureTags;

    public DeviceDto() {
    }

    public DeviceDto(Long id, String name, String brand, Set<String> featureTags) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.featureTags = featureTags;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Set<String> getFeatureTags() {
        return featureTags;
    }

    public void setFeatureTags(Set<String> featureTags) {
        this.featureTags = featureTags;
    }
}
