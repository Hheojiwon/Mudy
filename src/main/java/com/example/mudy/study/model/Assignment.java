package com.example.mudy.study.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Assignment {
    private final String userId;
    private String title;
    private LocalDateTime deadline;
    private boolean completed;

    public Assignment(String userId, String title, LocalDateTime deadline) {
        this.userId = userId;
        this.title = title;
        this.deadline = deadline;
        this.completed = false;
    }

    public void setCompleted() {
        completed = true;
    }
}
