package com.interviewbuddy.controller;

import com.interviewbuddy.dto.TopicBreakdownDto;
import com.interviewbuddy.dto.WeakAreaDto;
import com.interviewbuddy.dto.WeeklyStreakDto;
import com.interviewbuddy.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<TopicBreakdownDto>> summary() {
        return ResponseEntity.ok(service.getSummary());
    }

    @GetMapping("/weak-areas")
    public ResponseEntity<List<WeakAreaDto>> weakAreas() {
        return ResponseEntity.ok(service.getWeakAreas());
    }

    @GetMapping("/streak")
    public ResponseEntity<List<WeeklyStreakDto>> streak() {
        return ResponseEntity.ok(service.getStreak());
    }
}
