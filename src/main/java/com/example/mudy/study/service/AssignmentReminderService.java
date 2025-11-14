package com.example.mudy.study.service;

import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.repository.AssignmentRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentReminderService {
    private final AssignmentRepository assignmentRepository;

    public AssignmentReminderService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Scheduled(fixedRate = 1000)
    public void checkEachDeadlines() {
        List<Assignment> assignments = assignmentRepository.getAssignments();

        LocalDateTime now = LocalDateTime.now();

        assignments.stream()
                .filter(a -> !a.isCompleted())
                .filter(a -> {
                    long secondsLeft = Duration.between(now, a.getDeadline()).toSeconds();
                    return secondsLeft == 3600;
                })
                .forEach(this::notifyHourLeft);

        assignments.stream()
                .filter(a -> !a.isCompleted())
                .filter(a -> {
                    long secondsLeft = Duration.between(now, a.getDeadline()).toSeconds();
                    return secondsLeft == 0;
                })
                .forEach(this::notifyOver);
    }

    public void notifyHourLeft(Assignment assignment) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("⏰ **과제 마감 1시간 전** ⏰")
                .setColor(0xFF0000)
                .addField(assignment.getUserName() + "님 과제를 완료해주세요.", "- " + assignment.getTitle(), true);

        assignment.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    public void notifyOver(Assignment assignment) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("🚨 **과제가 마감되었습니다** 🚨")
                .setColor(0xFF0000)
                .addField(assignment.getUserName() + "님이 기한 안에 과제를 완료 못하셨습니다.", "- " + assignment.getTitle(), true);

        assignment.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}
