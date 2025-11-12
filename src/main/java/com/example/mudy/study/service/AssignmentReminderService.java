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

    @Scheduled(fixedRate = 60000)
    public void checkEachDeadlines() {
        List<Assignment> allAssignments = assignmentRepository.getAssignments();

        LocalDateTime now = LocalDateTime.now();

        allAssignments.stream()
                .filter(a -> !a.isCompleted())
                .filter(a -> {
                    long minutesLeft = Duration.between(now, a.getDeadline()).toMinutes();
                    return minutesLeft <= 60 && minutesLeft > 59;
                })
                .forEach(this::notifyUser);
    }

    public void notifyUser(Assignment assignment) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("⏰ **과제 마감 1시간 전** ⏰")
                .addField(assignment.getUserName() + "님 과제를 완료해주세요.", "- " + assignment.getTitle(), true);

        assignment.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}
