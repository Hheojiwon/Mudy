package com.example.mudy.study.model;

import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;

@Getter
public class Assignment {
    private final String userId;
    private String title;
    private LocalDateTime deadline;
    private boolean completed;
    private String userName;
    private TextChannel channel;

    public Assignment(String userId, String title, LocalDateTime deadline, String userName, TextChannel channel) {
        this.userId = userId;
        this.title = title;
        this.deadline = deadline;
        this.userName = userName;
        this.channel = channel;
        this.completed = false;
    }

    public void setCompleted() {
        completed = true;
    }
}
