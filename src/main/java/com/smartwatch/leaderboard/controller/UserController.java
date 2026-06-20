package com.smartwatch.leaderboard.controller;

import com.smartwatch.leaderboard.dto.TelemetryEventDto;
import com.smartwatch.leaderboard.dto.TelemetryMessage;
import com.smartwatch.leaderboard.dto.UserResponseDto;
import com.smartwatch.leaderboard.service.TelemetryProducer;
import com.smartwatch.leaderboard.service.UserService;
import com.smartwatch.leaderboard.service.DeviceService;
import com.smartwatch.leaderboard.repository.DeviceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TelemetryProducer telemetryProducer;
    private final DeviceService deviceService;
    private final DeviceRepository deviceRepository;

    public UserController(
            UserService userService,
            TelemetryProducer telemetryProducer,
            DeviceService deviceService,
            DeviceRepository deviceRepository
    ) {
        this.userService = userService;
        this.telemetryProducer = telemetryProducer;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> publishTelemetry(
            @PathVariable Long userId,
            @Valid @RequestBody TelemetryEventDto dto
    ) {
        // Enforce data validation and hardware capability verification if needed
        TelemetryMessage message = new TelemetryMessage(
                userId,
                dto.getStepCountValue(),
                dto.getRequiredTagValue(),
                dto.getTaskId(),
                dto.getChallengeId(),
                dto.getDate()
        );

        // Publish to Kafka queue
        telemetryProducer.sendTelemetryEvent(message);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Smartwatch telemetry published to ingestion queue successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    // Helper endpoint to easily register/assign a smartwatch device to a User profile in Swagger
    @PostMapping("/{userId}/device/{deviceId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> registerUserDevice(
            @PathVariable Long userId,
            @PathVariable Long deviceId
    ) {
        userService.registerUserDevice(userId, deviceId, deviceService, deviceRepository);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Smartwatch device assigned to user profile successfully");
        return ResponseEntity.ok(response);
    }
}
