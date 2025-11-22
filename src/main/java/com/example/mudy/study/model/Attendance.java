package com.example.mudy.study.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Attendance {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userId;

    public Attendance(LocalDateTime startTime, String userId) {
        this.startTime = startTime;
        this.endTime = startTime;
        this.userId = userId;
    }

    public void end(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
