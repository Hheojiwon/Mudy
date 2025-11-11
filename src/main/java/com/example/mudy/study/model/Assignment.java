package com.example.mudy.study.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Assignment {
    private final String userId;
    private final String title;
    private final LocalDateTime deadline;

    public Assignment(String userId, String title, LocalDateTime deadline) {
        this.userId = userId;
        this.title = title;
        this.deadline = deadline;
    }
}
