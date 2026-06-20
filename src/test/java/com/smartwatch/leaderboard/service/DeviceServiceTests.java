package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.DeviceDto;
import com.smartwatch.leaderboard.model.Device;
import com.smartwatch.leaderboard.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeviceServiceTests {

    private DeviceRepository deviceRepository;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceRepository = mock(DeviceRepository.class);
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void testCreateDevice() {
        DeviceDto dto = new DeviceDto(null, "Watch Series 9", "Apple", Set.of("GPS", "HRM"));
        Device savedDevice = new Device(1L, "Watch Series 9", "Apple", Set.of("GPS", "HRM"));

        when(deviceRepository.save(any(Device.class))).thenReturn(savedDevice);

        DeviceDto result = deviceService.createDevice(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Watch Series 9", result.getName());
        assertEquals("Apple", result.getBrand());
        assertTrue(result.getFeatureTags().contains("GPS"));
    }

    @Test
    void testGetAllDevices() {
        Device device = new Device(1L, "Galaxy Watch 6", "Samsung", Set.of("GPS"));
        when(deviceRepository.findAll()).thenReturn(List.of(device));

        List<DeviceDto> result = deviceService.getAllDevices();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Galaxy Watch 6", result.get(0).getName());
    }

    @Test
    void testUpdateDeviceSuccess() {
        Device existing = new Device(1L, "Original", "Brand", Set.of("GPS"));
        DeviceDto updateDto = new DeviceDto(1L, "Updated", "BrandNew", Set.of("HRM"));
        Device updated = new Device(1L, "Updated", "BrandNew", Set.of("HRM"));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(deviceRepository.save(any(Device.class))).thenReturn(updated);

        DeviceDto result = deviceService.updateDevice(1L, updateDto);

        assertNotNull(result);
        assertEquals("Updated", result.getName());
        assertEquals("BrandNew", result.getBrand());
        assertTrue(result.getFeatureTags().contains("HRM"));
    }

    @Test
    void testUpdateDeviceThrowsExceptionIfNotFound() {
        DeviceDto updateDto = new DeviceDto(1L, "Updated", "BrandNew", Set.of("HRM"));
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> deviceService.updateDevice(1L, updateDto));
    }
}
