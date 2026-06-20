package com.smartwatch.leaderboard.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "device_feature_tags", joinColumns = @JoinColumn(name = "device_id"))
    @Column(name = "feature_tag")
    private Set<String> featureTags = new HashSet<>();

    public Device() {
    }

    public Device(Long id, String name, String brand, Set<String> featureTags) {
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

    // Manual Builder Pattern
    public static DeviceBuilder builder() {
        return new DeviceBuilder();
    }

    public static class DeviceBuilder {
        private Long id;
        private String name;
        private String brand;
        private Set<String> featureTags = new HashSet<>();

        public DeviceBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DeviceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DeviceBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public DeviceBuilder featureTags(Set<String> featureTags) {
            this.featureTags = featureTags;
            return this;
        }

        public Device build() {
            return new Device(id, name, brand, featureTags);
        }
    }
}
