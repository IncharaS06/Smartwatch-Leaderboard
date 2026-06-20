package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.DeviceDto;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional(readOnly = true)
    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceDto createDevice(DeviceDto dto) {
        Device device = Device.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .featureTags(dto.getFeatureTags())
                .build();
        Device saved = deviceRepository.save(device);
        return mapToDto(saved);
    }

    @Transactional
    public DeviceDto updateDevice(Long id, DeviceDto dto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + id));

        device.setName(dto.getName());
        device.setBrand(dto.getBrand());
        device.setFeatureTags(dto.getFeatureTags());

        Device saved = deviceRepository.save(device);
        return mapToDto(saved);
    }

    private DeviceDto mapToDto(Device device) {
        return new DeviceDto(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getFeatureTags()
        );
    }
}
