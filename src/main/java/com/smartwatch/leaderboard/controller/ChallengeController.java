package com.smartwatch.leaderboard.controller;

import com.smartwatch.leaderboard.dto.ChallengeDto;
import com.smartwatch.leaderboard.dto.ChallengeUserResponseDto;
import com.smartwatch.leaderboard.model.User;
import com.smartwatch.leaderboard.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ChallengeDto> createChallenge(@Valid @RequestBody ChallengeDto dto) {
        return new ResponseEntity<>(challengeService.createChallenge(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ChallengeDto>> discoverChallenges(@RequestParam(required = false) Long userId) {
        Long targetUserId = userId;
        if (targetUserId == null) {
            // Retrieve the logged-in user details directly from the security context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User loggedInUser = (User) auth.getPrincipal();
            targetUserId = loggedInUser.getId();
        }
        return ResponseEntity.ok(challengeService.getDiscoverableChallengesForUser(targetUserId));
    }

    @GetMapping("/{challengeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ChallengeDto> getChallengeById(@PathVariable Long challengeId) {
        return ResponseEntity.ok(challengeService.getChallengeById(challengeId));
    }

    @GetMapping("/{challengeId}/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ChallengeUserResponseDto>> getPaginatedChallengeUsers(
            @PathVariable Long challengeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(challengeService.getPaginatedChallengeUsers(challengeId, page, size));
    }

    @PutMapping("/{challengeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChallengeDto> updateChallenge(@PathVariable Long challengeId, @Valid @RequestBody ChallengeDto dto) {
        return ResponseEntity.ok(challengeService.updateChallenge(challengeId, dto));
    }

    @PostMapping("/{challengeId}/join")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> joinChallenge(
            @PathVariable Long challengeId,
            @RequestParam(required = false) Long userId) {
        Long targetUserId = userId;
        if (targetUserId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User loggedInUser = (User) auth.getPrincipal();
            targetUserId = loggedInUser.getId();
        }

        challengeService.joinChallenge(challengeId, targetUserId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Joined challenge successfully! Your hardware capabilities have been verified and approved.");
        return ResponseEntity.ok(response);
    }
}
