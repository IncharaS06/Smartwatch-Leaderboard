package com.smartwatch.leaderboard.controller;

import com.smartwatch.leaderboard.dto.DeviceDto;
import com.smartwatch.leaderboard.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceDto dto) {
        return new ResponseEntity<>(deviceService.createDevice(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{deviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceDto> updateDeviceWithId(@PathVariable Long deviceId, @Valid @RequestBody DeviceDto dto) {
        return ResponseEntity.ok(deviceService.updateDevice(deviceId, dto));
    }

    // Handles PUT /device fallback as requested in endpoint requirements
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceDto> updateDevice(@Valid @RequestBody DeviceDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Device id must be provided for update");
        }
        return ResponseEntity.ok(deviceService.updateDevice(dto.getId(), dto));
    }
}
