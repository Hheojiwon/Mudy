package com.example.mudy.study.model;

import lombok.Getter;

import java.time.Duration;

@Getter
public class UserAttend {
    private String userId;
    private String userName;
    private Duration time;

    public UserAttend(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.time = Duration.ofMinutes(0);
    }

    public void addTime(Duration time) {
        this.time = this.time.plus(time);
    }
}
